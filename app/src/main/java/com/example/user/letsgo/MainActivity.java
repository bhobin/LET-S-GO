package com.example.user.letsgo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    DrawerLayout drawerLayout;
    ListView listView;
    ImageView mainMenu;
    private Typeface mTypeface;
    private long lastTimeBackPressed;
    ImageView homebutton;
    RelativeLayout moveLayout02;
    RelativeLayout moveLayout03;
    RelativeLayout moveLayout04;
    RelativeLayout main_menu_layout;
    View dialogView;
    boolean isPageOpen = true;
    int openCount = 0;

    private String id = "";

    ActionBarDrawerToggle drawerToggle;
    String[] drawer_str = {"공지사항", "도움말", "어플 소개", "만든 사람들", "로그아웃"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mainIntent = getIntent();
        id = mainIntent.getStringExtra("id");
        Utils.id = id;
        firstChecked();

        mTypeface = Typeface.createFromAsset(getAssets(), "BMDOHYEON_ttf.ttf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        Utils.setGlobalFont(root, mTypeface);

        main_menu_layout = (RelativeLayout) findViewById(R.id.main_menu_layout);
        main_menu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        moveLayout02 = (RelativeLayout) findViewById(R.id.moveLayout02);
        moveLayout02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoadActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });
        moveLayout03 = (RelativeLayout) findViewById(R.id.moveLayout03);
        moveLayout03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RecentDownActivity.class);
                startActivity(intent);
            }
        });
        moveLayout04 = (RelativeLayout) findViewById(R.id.moveLayout04);
        moveLayout04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BestDownActivity.class);
                startActivity(intent);
            }
        });


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        listView = (ListView) findViewById(R.id.drawer);

        List<String> drawer_str_list = new ArrayList<String>(Arrays.asList(drawer_str));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, drawer_str_list) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                // Set the text size 25 dip for ListView each item
                tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                //tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tv.setTextColor(Color.parseColor("#f62a2a2a"));
                // Return the view

                //tv.setTypeface(mTypeface);
                //메뉴의 font 설정
                return view;
            }
        };
        listView.setAdapter(adapter);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        drawerLayout.setDrawerListener(drawerToggle);

        mainMenu = (ImageView) findViewById(R.id.menubutton);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPageOpen) {
                    drawerLayout.openDrawer(GravityCompat.START);
                    isPageOpen = false;
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    isPageOpen = true;
                }
            }
        });

        homebutton = (ImageView) findViewById(R.id.homebutton);
        homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drawerLayout.closeDrawer(GravityCompat.START);
                ShowLogoutDialog();
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), NoticeActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(getApplicationContext(), HelpViewpagerActivity.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawers();
                        break;
                    case 2:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        onCoachMark();
                        break;
                    case 3:
                        dialogView = getLayoutInflater().inflate(R.layout.dialog_make, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setView(dialogView);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        break;
                    case 4:
                        ShowLogoutDialog();
                    default:
                        break;
                }
            }
        });
    }

    public void ShowLogoutDialog() {

        LayoutInflater dialog = LayoutInflater.from(this);
        final View dialogLayout = dialog.inflate(R.layout.dialog_logout, null);
        final Dialog myDialog = new Dialog(this);

        myDialog.setContentView(dialogLayout);
        myDialog.show();

        Button btn_ok = (Button) dialogLayout.findViewById(R.id.button_ok_dialog_logout);
        Button btn_cancel = (Button) dialogLayout.findViewById(R.id.button_cancel_dialog_logout);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
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

    public void onCoachMark() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.coach_mark);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(android.view.WindowManager.LayoutParams.MATCH_PARENT, android.view.WindowManager.LayoutParams.MATCH_PARENT);
        View masterView = dialog.findViewById(R.id.coach_mark_master_view);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void firstChecked() {
        SharedPreferences pref = getSharedPreferences("pref", 0);
        // SharedPreferences 생성, pref에 저장

        String str = pref.getString("checked", "first");
        // pref에서 문자열을 읽어오는 메소드. key : checked, value : null일 때 first로 지정

        if (str.equals("first")) {
            onCoachMark();
        }
        // value가 null일 때, 즉 최초 실행시에만 수행

        SharedPreferences.Editor editer = pref.edit();
        editer.putString("checked", "many");
        editer.commit();
        // 최초 실행 이후 value값 변경. commit() 함수 꼭 해줘야 저장됨.
    }
    // 최초 실행시에만 if구절 실행
}