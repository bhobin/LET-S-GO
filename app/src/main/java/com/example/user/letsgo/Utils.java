package com.example.user.letsgo;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jo on 2016-07-22.
 */
public class Utils {
    public static String id = ""; //로그인된 아이디

    public static void setGlobalFont(ViewGroup root, Typeface mTypeface) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View child = root.getChildAt(i);
            if (child instanceof TextView)
                ((TextView) child).setTypeface(mTypeface);
            else if (child instanceof ViewGroup)
                setGlobalFont((ViewGroup) child, mTypeface);
        }
    }
    // 레이아웃 전체 폰트 설정
}
