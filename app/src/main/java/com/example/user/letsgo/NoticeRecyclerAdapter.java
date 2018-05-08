package com.example.user.letsgo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by kwanghee on 2016-09-01.
 */
public class NoticeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int VIEW_TYPE_CONTENT = 1;

    public NoticeRecyclerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == VIEW_TYPE_CONTENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
            viewHolder = new ItemViewHolder(view);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_CONTENT) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        int checkOpen=0;
        ImageView icon_notice_close;
        ImageView icon_notice_open;
        LinearLayout notice_layout;
        LinearLayout notice_layout2;
        public ItemViewHolder(View v) {
            super(v);
            notice_layout = (LinearLayout) v.findViewById(R.id.notice_layout);
            notice_layout2 = (LinearLayout) v.findViewById(R.id.notice_layout2);
            icon_notice_close = (ImageView) v.findViewById(R.id.icon_notice_close);
            icon_notice_open = (ImageView) v.findViewById(R.id.icon_notice_open);

            notice_layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(checkOpen==0) {
                        notice_layout2.setVisibility(View.VISIBLE);
                        icon_notice_close.setVisibility(View.GONE);
                        icon_notice_open.setVisibility(View.VISIBLE);
                        checkOpen = 1;
                    }
                    else if(checkOpen==1){
                        notice_layout2.setVisibility(View.GONE);
                        icon_notice_open.setVisibility(View.GONE);
                        icon_notice_close.setVisibility(View.VISIBLE);
                        checkOpen = 0;
                    }
                }
            });

        }
    }
}
