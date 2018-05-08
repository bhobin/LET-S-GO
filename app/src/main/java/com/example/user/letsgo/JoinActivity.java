package com.example.user.letsgo;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;

public class JoinActivity extends Activity {
    private AQuery aq = new AQuery(this);

    private EditText idEdit;
    private EditText pwEdit;
    private EditText pw2Edit;
    private EditText nameEdit;
    private EditText emailEdit;

    private Button idCheckButton;
    private boolean isIdDuplicated = true; // true는 아이디가 중복된상태.


    ImageView join_image_back;
    Button join_button_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        idEdit = (EditText) findViewById(R.id.id_edit_join);
        pwEdit = (EditText) findViewById(R.id.pw_edit_join);
        pw2Edit = (EditText) findViewById(R.id.pw2_edit_join);
        nameEdit = (EditText) findViewById(R.id.name_edit_join);
        emailEdit = (EditText) findViewById(R.id.email_edit_join);
        idCheckButton = (Button) findViewById(R.id.check_button_join);

        join_image_back = (ImageView) findViewById(R.id.join_image_back);

        join_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        idCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEdit.getText().toString();
                if (id.length() >= 2 && id.length() <= 14) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    aq.ajax("http://letsgo.woobi.co.kr/idcheck.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                        @Override
                        public void callback(String url, JSONObject object, AjaxStatus status) {
                            if (object != null) {
                                try {
                                    String result = object.getString("result");
                                    if (result.equals("possible")) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                        builder.setMessage("사용가능한 아이디입니다.\n사용하시겠습니까?");
                                        builder.setCancelable(false);
                                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                //dialog.dismiss();
                                            }
                                        });
                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                isIdDuplicated = false;
                                                idEdit.setEnabled(false);
                                                idCheckButton.setEnabled(false);
                                                idCheckButton.setBackgroundResource(R.drawable.join_checkbox_pressed);
                                                //dialog.dismiss();
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    } else if (result.equals("impossible")) {
                                        Toast toast = Toast.makeText(JoinActivity.this, "사용중인 아이디입니다.", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.CENTER, 0, -550);
                                        toast.show();
                                    }

                                } catch (Exception e) {
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(JoinActivity.this, "아이디는 2자이상 14자 이하만 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        join_button_join = (Button) findViewById(R.id.join_button_join);
        join_button_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEdit.getText().toString();
                String pw = pwEdit.getText().toString();
                String pw2 = pw2Edit.getText().toString();
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();

                if (isIdDuplicated) {
                    Toast.makeText(JoinActivity.this, "아이디 중복체크를 해주세요.", Toast.LENGTH_SHORT).show();
                } else if (!pw.equals(pw2)) {
                    Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                } else if (pw.length() < 2 || pw.length() > 14) {
                    Toast.makeText(JoinActivity.this, "비밀번호는 2자이상, 14자 이하만 가능합니다.", Toast.LENGTH_SHORT).show();
                } else if (name.length() < 2 || name.length() > 20) {
                    Toast.makeText(JoinActivity.this, "이름은 2자이상, 20자 이하만 가능합니다.", Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@") || !email.contains(".")) {
                    Toast.makeText(JoinActivity.this, "잘못된 이메일 주소입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", id);
                    map.put("password", pw);
                    map.put("name", name);
                    map.put("email", email);
                    aq.ajax("http://letsgo.woobi.co.kr/join.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                        public void callback(String url, JSONObject object, AjaxStatus status) {
                            if (object != null) {
                                try {
                                    String result = object.getString("result");
                                    if (result.equals("success")) {
                                        finish();
                                        Toast toast = Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                                        toast.show();
                                    } else if (result.equals("fail")) {
                                        Toast.makeText(JoinActivity.this, "에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(JoinActivity.this, "서버에 접속할수 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {

                                }
                            } else {
                                Toast.makeText(JoinActivity.this, "서버에 접속할수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
