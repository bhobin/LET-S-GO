package com.example.user.letsgo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by jo on 2016-07-26.
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private AQuery aq;
    private String compStr = "";

    // ListViewAdapter의 생성자
    public ListViewAdapter(AQuery aq, String compStr) {
        this.aq = aq;
        this.compStr = compStr;
    }

    public ListViewAdapter(AQuery aq) {
        this.aq = aq;
    }

    public ListViewAdapter() {

    }

    public void sortByDownCount() {
        Collections.sort(listViewItemList, new Comparator<ListViewItem>() {
            @Override
            public int compare(ListViewItem t1, ListViewItem t2) {
                int a = Integer.parseInt(t1.getDownStr());
                int b = Integer.parseInt(t2.getDownStr());
                if (a > b) return -1;
                else if (a == b) return 0;
                else return 1;
            }
        });
    }

    public void sortByDate() {
        Collections.sort(listViewItemList, new Comparator<ListViewItem>() {
            @Override
            public int compare(ListViewItem t1, ListViewItem t2) {
                long a = Long.parseLong(t1.getDownStr());
                long b = Long.parseLong(t2.getDownStr());
                if (a > b) return -1;
                else if (a == b) return 0;
                else return 1;
            }
        });
    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_server_items, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.listview_server_image);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.listview_server_title);
        TextView idTextView = (TextView) convertView.findViewById(R.id.listview_server_id);
        TextView downTextView = (TextView) convertView.findViewById(R.id.listview_server_down);

        //업로드일자, 다운로드 횟수 갈아껴주는 텍스트뷰
        TextView compTextView = (TextView) convertView.findViewById(R.id.listview_server_comp);
        compTextView.setText(compStr);


        ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.listview_progress);


        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable(listViewItem.getIconDrawable());
        titleTextView.setText(listViewItem.getTitleStr());
        idTextView.setText(listViewItem.getIdStr());
        downTextView.setText(listViewItem.getDownStr());


        AQuery imgaq = aq.recycle(convertView);
        imgaq.id(iconImageView).progress(progressBar).image(listViewItem.getUrl(), true, true);
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public ListViewItem getItem(int position) {
        return listViewItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String title, String id, String down, String url, String idx) {
        ListViewItem item = new ListViewItem();

        //item.setIconDrawble(icon);
        item.setTitleStr(title);
        item.setIdStr(id);
        item.setDownStr(down);

        item.setUrl(url);

        item.setIdx(idx);


        listViewItemList.add(item);
    }

    public void removeAll(){
        listViewItemList.removeAll(listViewItemList);
        notifyDataSetChanged();
    }
}