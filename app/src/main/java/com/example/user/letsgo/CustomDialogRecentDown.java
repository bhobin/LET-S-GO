package com.example.user.letsgo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by jo on 2016-08-04.
 */

//pull to refresh 적용후 아이템이 하나씩 밀려져서나옴... 인덱스 초과함
public class CustomDialogRecentDown extends Dialog {
    private AQuery aq = null;
    private ImageView img;
    private Button okBtn;
    private Button cancelBtn;
    private String id = "";
    private String idx = "";

    private String response = "";
    private String[] responseArr;

    private Thread t = null;

    private String URL; //getIconDrawble 안먹혀서 AQ로 우회...(이미지를 다시 다운받는 형식)

    public CustomDialogRecentDown(Context context, AQuery aq, String URL, String idx) {
        super(context);
        this.aq = aq;
        this.URL = URL;
        this.idx = idx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recent_down);

        img = (ImageView) findViewById(R.id.image_dialog_recent_down);
        okBtn = (Button) findViewById(R.id.button_ok_dialog_recent_down);
        cancelBtn = (Button) findViewById(R.id.button_cancel_dialog_recent_down);


        this.id = Utils.id;
        aq.id(img).image(URL);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);
                //db상의 member테이블에 있는 download필드를 가져오는 php파일
                aq.ajax("http://letsgo.woobi.co.kr/loadfrag2_1.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        if (object != null) {
                            try {
                                boolean isDownloaded = false;
                                response = object.getString("result");
                                responseArr = response.split(",");
                                for (int i = 0; i < responseArr.length; i++) {
                                    if (responseArr[i].equals(idx) && !responseArr[0].equals("")) {
                                        Toast.makeText(getContext(), "이미 다운로드된 파일입니다.", Toast.LENGTH_SHORT).show();
                                        isDownloaded = true;
                                        dismiss();
                                        break;
                                    }
                                }
                                if (!isDownloaded) {
                                    if (t != null) t.start();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        t = new Thread() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                String insertStr = response + idx + ",";
                map.put("id", id);
                map.put("insertstr", insertStr);
                map.put("idx", idx);
                aq.ajax("http://letsgo.woobi.co.kr/insertdownload.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        if (object != null) {
                            try {
                                if (object.getString("result").equals("success")) {
                                    Toast.makeText(getContext(), "다운로드 되었습니다.", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                } else if (object.getString("result").equals("fail")) {
                                    Toast.makeText(getContext(), "다운로드에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "서버와의 통신에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        };
    }

}
