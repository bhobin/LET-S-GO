package com.example.user.letsgo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
public class CustomDialogLoadUpload extends Dialog {
    private AQuery aq = null;
    private ImageView img;
    private Button printBtn;
    private Button cancelBtn;
    private Button deleteBtn;

    private String id = "";
    private String idx = "";

    private String response = "";
    private String[] responseArr;

    private Thread t = null;

    private String URL; //getIconDrawble 안먹혀서 AQ로 우회...(이미지를 다시 다운받는 형식)

    public CustomDialogLoadUpload(Context context, AQuery aq, String URL, String idx) {
        super(context);
        this.aq = aq;
        this.URL = URL;
        this.idx = idx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_load_upload);

        img = (ImageView) findViewById(R.id.image_dialog_load_upload);
        printBtn = (Button) findViewById(R.id.button_print_dialog_load_upload);
        cancelBtn = (Button) findViewById(R.id.button_cancel_dialog_load_upload);
        deleteBtn = (Button) findViewById(R.id.button_delete_dialog_load_upload);

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
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("삭제 확인");
                builder.setMessage("정말로 삭제하시겠습니까?\n삭제한 데이터는 복구할 수 없습니다.");
                builder.setCancelable(true);
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("idx", idx);
                        aq.ajax("http://letsgo.woobi.co.kr/uploaddelete.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                            @Override
                            public void callback(String url, JSONObject object, AjaxStatus status) {
                                if (object != null) {
                                    try {
                                        if (object.getString("result").equals("fail")) {
                                            Toast.makeText(getContext(), "서버와 통신할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                        } else if (object.getString("result").equals("success")) {
                                            Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                            dismiss();
                                            Load_frag1.loadfrag1.refresh();
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
                    }
                });
                android.support.v7.app.AlertDialog dialog = builder.create();
                dialog.show();
                //TODO 삭제버튼 마무리
                //TODO frag2와는 다르게 자신이 업로드한 파일을 삭제하는것이므로 핸드폰에서 지우는것 뿐만 아니라 서버상에서도 지워지게해야함
                //TODO ALERT DIALOG를 띄워서 "TITLE: 삭제 확인   CONTENT: 정말로 삭제하시겠습니까? 삭제한 데이터는 복구할 수 없습니다." 사용자에게 확인을 한번 더 받자.
                //TODO 확인버튼 누르면 프로그레스바 띄우고 새로고침...하자 (위에 참고)
            }
        });

    }

}
