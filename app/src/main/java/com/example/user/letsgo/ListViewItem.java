package com.example.user.letsgo;

import android.graphics.drawable.Drawable;

/**
 * Created by jo on 2016-07-26.
 */

public class ListViewItem {
    private Drawable iconDrawable;
    private String titleStr;
    private String idStr;
    private String downStr;

    private String url;

    private String idx;

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getDownStr() {
        return downStr;
    }

    public void setDownStr(String downStr) {
        this.downStr = downStr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }


}