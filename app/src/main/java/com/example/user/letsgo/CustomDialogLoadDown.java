package com.example.user.letsgo;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
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
public class CustomDialogLoadDown extends Dialog {
    private AQuery aq = null;
    private ImageView img;
    private Button printBtn;
    private Button cancelBtn;
    private Button deleteBtn;

    private String id = "";
    private String idx = "";

    private String str = "";
    private String[] responseArr;

    private Thread t = null;

    private String URL; //getIconDrawble 안먹혀서 AQ로 우회...(이미지를 다시 다운받는 형식)

    public CustomDialogLoadDown(Context context, AQuery aq, String URL, String idx, String[] responseArr) {
        super(context);
        this.aq = aq;
        this.URL = URL;
        this.idx = idx;
        this.responseArr = responseArr;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_load_down);

        img = (ImageView) findViewById(R.id.image_dialog_load_down);
        printBtn = (Button) findViewById(R.id.button_print_dialog_load_down);
        cancelBtn = (Button) findViewById(R.id.button_cancel_dialog_load_down);
        deleteBtn = (Button) findViewById(R.id.button_delete_dialog_load_down);

        this.id = Utils.id;
        aq.id(img).image(URL);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "추가 예정중인 기능입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //TODO delete 버튼 누르면 progressBar 띄우고 adapter.notify...로 새로고침해서 에러가 안나게 구현해야할듯. 아니면 클라이언트 단에서 예외처리를 해버려도 되고...아니면 둘다?

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(), "추가 예정중인 기능입니다.", Toast.LENGTH_SHORT).show();
                for (int i = 0; i < responseArr.length; i++) {
                    if (!responseArr[i].equals(idx)) {
                        str = str + responseArr[i] + ",";
                    }
                }

                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("str", str);

                aq.ajax("http://letsgo.woobi.co.kr/downdelete.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                    @Override
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        if (object != null) {
                            try {
                                if (object.getString("result").equals("fail")) {
                                    Toast.makeText(getContext(), "서버와 통신할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                } else if (object.getString("result").equals("success")) {
                                    Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    Load_frag2.loadfrag2.refresh();
                                } else {
                                    Toast.makeText(getContext(), "서버와 통신할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getContext(), "서버와 통신할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "서버와 통신할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                //TODO 삭제버튼 마무리
                //TODO frag1과는 다르게 찜한목록(?)(북마크 느낌)을 삭제하는것이므로 핸드폰에서만 지우자. DB내 MEMBER 테이블의 DOWNLOAD 필드에서 파싱후 지워지는 방식으로 구현해야할듯.
            }
        });

    }
}