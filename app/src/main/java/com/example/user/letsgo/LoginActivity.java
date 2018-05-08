package com.example.user.letsgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends Activity {
    private AQuery aq = new AQuery(this);
    private EditText idEdit;
    private EditText pwEdit;

    private long lastTimeBackPressed;
    Button loginbutton;
    TextView login_text_join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Utils.id = "";

        idEdit = (EditText) findViewById(R.id.login_edit_id);
        pwEdit = (EditText) findViewById(R.id.login_edit_pw);

        loginbutton = (Button) findViewById(R.id.loginbutton);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = idEdit.getText().toString();
                String pw = pwEdit.getText().toString();
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);
                map.put("password", pw);
                aq.ajax("http://letsgo.woobi.co.kr/login.php", map, JSONObject.class, new AjaxCallback<JSONObject>() {
                    public void callback(String url, JSONObject object, AjaxStatus status) {
                        if (object != null) {
                            try {
                                String result = object.getString("result");
                                if (result.equals("success")) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra("id", idEdit.getText().toString());
                                    startActivity(intent);
                                } else if (result.equals("fail")) {
                                    Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, "서버에 접속할수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "서버에 접속할수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        login_text_join = (TextView) findViewById(R.id.login_text_join);
        login_text_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastTimeBackPressed < 1500) {
            finish();
            return;
        }
        Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        lastTimeBackPressed = System.currentTimeMillis();
    }
    // 뒤로가기 구현
}
