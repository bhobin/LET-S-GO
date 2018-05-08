package com.example.user.letsgo;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Load_frag1 extends Fragment {
    public static Load_frag1 loadfrag1;

    private AQuery aq;
    private String id = "";
    private int listCount = 0;
    private Thread t;

    private PullToRefreshListView listview;
    private ListViewAdapter adapter;

    private ImageView emptyImage;
    private TextView emptyText1;
    private TextView emptyText2;

    private Typeface mTypeface;

    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.removeAll();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(800);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Call onRefreshComplete when the list has been refreshed.
            listview.onRefreshComplete();
            super.onPostExecute(result);
            start();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.load_frag1, container, false);

        loadfrag1 = this;

        emptyImage = (ImageView) rootView.findViewById(R.id.load_frag1_empty_image);
        emptyText1 = (TextView) rootView.findViewById(R.id.load_frag1_empty_text1);
        emptyText2 = (TextView) rootView.findViewById(R.id.load_frag1_empty_text2);

        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "BMDOHYEON_ttf.ttf");

        aq = new AQuery(rootView);

        Bundle extra = getArguments();
        id = extra.getString("id");

        // Adapter 생성
        adapter = new ListViewAdapter(aq, "업로드 일자");

        // 리스트뷰 참조 및 Adapter달기
        listview = (PullToRefreshListView) rootView.findViewById(R.id.load_frag1_listView);
        listview.setAdapter(adapter);

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                refresh();
            }
        });

        start();

        return rootView;
    }

    public void refresh() {
        new GetDataTask().execute();
    }

    public void start() {
        HashMap<String, Object> idMap = new HashMap<>();
        idMap.put("id", id);
        aq.ajax("http://letsgo.woobi.co.kr/listcount.php", idMap, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                if (object != null) {
                    try {
                        if (!object.getString("result").contains("fail")) {
                            if (object.getString("result").equals("0")) {
                                //Toast.makeText(getContext(), "업로드한 목록이 없습니다.", Toast.LENGTH_SHORT).show();
                                emptyImage.setImageResource(R.drawable.empty);
                                emptyText1.setText("업로드한 목록이 없습니다");
                                emptyText2.setText("시뮬레이션 기능을 통해 자신만의 블럭을 만들어보세요!");
                                emptyText1.setTypeface(mTypeface);
                                emptyText2.setTypeface(mTypeface);
                            } else {
                                listCount = Integer.parseInt(object.getString("result"));
                                emptyText1.setText(null);
                                emptyText2.setText(null);
                                emptyImage.setImageDrawable(null);
                                t.start();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "서버에 접속할수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        t = new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < listCount; i++) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("index", i);
                    aq.ajax("http://letsgo.woobi.co.kr/loadfrag1.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                        public void callback(String url, JSONObject object, AjaxStatus status) {
                            if (object != null) {
                                try {
                                    String title = object.getString("title");
                                    String id = object.getString("id");
                                    String date = object.getString("date");
                                    String location = object.getString("location").replace('\\', '\r');
                                    String idx = object.getString("idx");
                                    adapter.addItem(title, id, date, location, idx);
                                    adapter.notifyDataSetChanged();

                                    if (adapter.getCount() == listCount) {
                                        adapter.sortByDate();
                                        for (int i = 0; i < adapter.getCount(); i++) {
                                            Date d = new Date(Long.parseLong(adapter.getItem(i).getDownStr()));
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                                            adapter.getItem(i).setDownStr(dateFormat.format(d));
                                            adapter.notifyDataSetChanged();
                                        }
                                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                CustomDialogLoadUpload cd = new CustomDialogLoadUpload(getContext(), aq, adapter.getItem(position - 1).getUrl(), adapter.getItem(position - 1).getIdx());
                                                cd.show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                }
                            }
                        }
                    });
                }
            }
        };
    }
}

