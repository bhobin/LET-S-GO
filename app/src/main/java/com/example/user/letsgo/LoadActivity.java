package com.example.user.letsgo;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//TODO Empty data 디자인 호빈이한테 부탁하기 (http://story.pxd.co.kr/718)
public class LoadActivity extends FragmentActivity {

    private Typeface mTypeface;
    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView down_back_image;

    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        Intent loadIntent = getIntent();
        id = loadIntent.getStringExtra("id");

        mTypeface = Typeface.createFromAsset(getAssets(), "BMDOHYEON_ttf.ttf");
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        Utils.setGlobalFont(root, mTypeface);

        down_back_image = (ImageView) findViewById(R.id.load_back_image);
        down_back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        viewPager = (ViewPager) findViewById(R.id.load_viewPager);
        viewPager.setAdapter(new CustomAdapter(getSupportFragmentManager()));

        tabLayout = (TabLayout) findViewById(R.id.load_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

    }

    private class CustomAdapter extends FragmentPagerAdapter {

        private String fragments[] = {"업로드 목록", "다운 목록"};

        public CustomAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Load_frag1 lf1 = new Load_frag1();
                    Bundle bundle = new Bundle();
                    bundle.putString("id", id);
                    lf1.setArguments(bundle);
                    return lf1;
                case 1:
                    Load_frag2 lf2 = new Load_frag2();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("id", id);
                    lf2.setArguments(bundle2);
                    return lf2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position];
        }
    }
}