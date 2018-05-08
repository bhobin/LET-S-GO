package com.example.user.letsgo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.androidquery.AQuery;

/**
 * Created by jo on 2016-08-04.
 */
public class CustomDialogLogout extends Dialog {

    public CustomDialogLogout(Context context, AQuery aq, String URL, String idx) {
        super(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_logout);


    }
}
