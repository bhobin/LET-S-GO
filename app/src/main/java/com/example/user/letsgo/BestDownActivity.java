package com.example.user.letsgo;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class BestDownActivity extends AppCompatActivity {
    private AQuery aq = new AQuery(this);
    private static BestDownActivity thisContext;

    private PullToRefreshListView listview;
    private ListViewAdapter adapter;

    private Typeface mTypeface;
    private ImageView down_back_image;

    private int listTotalSize;

    private class GetDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.removeAll();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1500);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_down);

        thisContext = this;

        mTypeface = Typeface.createFromAsset(getAssets(), "BMDOHYEON_ttf.ttf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        Utils.setGlobalFont(root, mTypeface);

        down_back_image = (ImageView) findViewById(R.id.down_back_image);
        down_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview = (PullToRefreshListView) findViewById(R.id.best_down_listView);
        adapter = new ListViewAdapter(aq, "다운로드 횟수");
        listview.setAdapter(adapter);

        listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
                new GetDataTask().execute();
            }
        });

        start();
    }

    public void start() {
        Thread listCountThread = new Thread() {
            @Override
            public void run() {
                aq.ajax("http://letsgo.woobi.co.kr/LegoTableCount.php", JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        if (object != null) {
                            try {
                                if (object.getString("result").equals("fail")) {
                                    Toast.makeText(BestDownActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    listTotalSize = Integer.parseInt(object.getString("result"));
                                    addItems(listTotalSize);
                                    //Toast.makeText(BestDownActivity.this, listTotalSize+"", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(BestDownActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        };
        listCountThread.start();
    }

    private void addItems(final int size) {
        Runnable run = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < size; i++) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("index", i);
                    aq.ajax("http://letsgo.woobi.co.kr/bestdown.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                        public void callback(String url, JSONObject object, AjaxStatus status) {
                            if (object != null) {
                                try {
                                    String title = object.getString("title");
                                    String id = object.getString("id");
                                    String count = object.getString("count");
                                    String location = object.getString("location").replace('\\', '\r');
                                    String idx = object.getString("idx");
                                    adapter.addItem(title, id, count, location, idx);
                                    adapter.notifyDataSetChanged();

                                    if (adapter.getCount() == listTotalSize) {
                                        adapter.sortByDownCount();
                                        adapter.notifyDataSetChanged();
                                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                                CustomDialogBestDown cd = new CustomDialogBestDown(thisContext, aq, adapter.getItem(position-1).getUrl(), adapter.getItem(position-1).getIdx());
                                                cd.show();
                                            }
                                        });
                                    }
                                } catch (Exception e) {
                                    //Toast.makeText(BestDownActivity.this, "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
                adapter.notifyDataSetChanged();
            }
        };

        // 속도의 딜레이를 구현하기 위한 꼼수
        Handler handler = new Handler();
        //handler.postDelayed(run, 1200);
        handler.post(run);
    }
}