package com.example.user.letsgo;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jo on 2016-08-18.
 */
public class HelpPageFragment extends Fragment {
    private int mPageNumber;
    private ImageView mainImage;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    private ImageView whiteCircleImage1;
    private ImageView whiteCircleImage2;
    private ImageView whiteCircleImage3;
    private ImageView whiteCircleImage4;
    private ImageView whiteCircleImage5;
    private ImageView whiteCircleImage6;

    private ImageView finishImage;
    private TextView finishText1;
    private TextView finishText2;


    private Typeface mTypeface;

    public static HelpPageFragment create(int pageNumber) {
        HelpPageFragment fragment = new HelpPageFragment();
        Bundle args = new Bundle();
        args.putInt("page", pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt("page");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_viewpager_help, container, false);

        mTypeface = Typeface.createFromAsset(getContext().getAssets(), "BMDOHYEON_ttf.ttf");
        textView1 = ((TextView) rootView.findViewById(R.id.help_text1));
        textView2 = ((TextView) rootView.findViewById(R.id.help_text2));
        textView3 = ((TextView) rootView.findViewById(R.id.help_text3));
        textView4 = ((TextView) rootView.findViewById(R.id.help_text4));

        mainImage = (ImageView) rootView.findViewById(R.id.help_main_image);

        finishImage = (ImageView) rootView.findViewById(R.id.help_finish_next);
        finishText1 = ((TextView) rootView.findViewById(R.id.help_finish_text));
        finishText2 = ((TextView) rootView.findViewById(R.id.help_finish_text2));

        whiteCircleImage1 = (ImageView) rootView.findViewById(R.id.white_circle1);
        whiteCircleImage2 = (ImageView) rootView.findViewById(R.id.white_circle2);
        whiteCircleImage3 = (ImageView) rootView.findViewById(R.id.white_circle3);
        whiteCircleImage4 = (ImageView) rootView.findViewById(R.id.white_circle4);
        whiteCircleImage5 = (ImageView) rootView.findViewById(R.id.white_circle5);
        whiteCircleImage6 = (ImageView) rootView.findViewById(R.id.white_circle6);


        textView1.setTypeface(mTypeface);
        textView2.setTypeface(mTypeface);
        textView3.setTypeface(mTypeface);
        textView4.setTypeface(mTypeface);

        finishText1.setTypeface(mTypeface);
        finishText2.setTypeface(mTypeface);

        whiteCircleImage1.setImageResource(R.drawable.white_circle);
        whiteCircleImage2.setImageResource(R.drawable.white_circle);
        whiteCircleImage3.setImageResource(R.drawable.white_circle);
        whiteCircleImage4.setImageResource(R.drawable.white_circle);
        whiteCircleImage5.setImageResource(R.drawable.white_circle);
        whiteCircleImage6.setImageResource(R.drawable.white_circle);


        switch (mPageNumber) {
            case 0:
                textView3.setText("Let's GO 시뮬레이션으로");
                textView4.setText("자신만의 블럭을 조립해보세요!");
                whiteCircleImage1.setImageResource(R.drawable.white_circle_full);
                break;
            case 1:
                textView2.setText("SIMULATION");
                textView3.setText("시뮬레이션 창을 통해");
                textView4.setText("블럭을 조립하거나 출력할 수 있습니다.");
                mainImage.setImageResource(R.drawable.pager2);
                whiteCircleImage2.setImageResource(R.drawable.white_circle_full);
                break;
            case 2:
                textView2.setText("DOWNLOAD");
                textView3.setText("다른 사람들이 만든");
                textView4.setText("블럭을 다운로드 받을 수 있습니다.");
                mainImage.setImageResource(R.drawable.pager3);
                whiteCircleImage3.setImageResource(R.drawable.white_circle_full);
                break;
            case 3:
                textView2.setText("LOAD");
                textView3.setText("업로드 & 다운로드한 블럭의");
                textView4.setText("정보를 확인해 볼 수 있습니다.");
                mainImage.setImageResource(R.drawable.pager4);
                whiteCircleImage4.setImageResource(R.drawable.white_circle_full);
                break;
            case 4:
                textView2.setText("WEB PAGE");
                textView3.setText("www.letsgo.woobi.co.kr 에서");
                textView4.setText("블럭을 관리,다운로드 받을 수 있습니다.");
                mainImage.setImageResource(R.drawable.webpage);
                whiteCircleImage5.setImageResource(R.drawable.white_circle_full);
                break;
            case 5:
                textView2.setText("PRINTING");
                textView3.setText("다운로드한 도면은");
                textView4.setText("3D프린터로 인쇄가 가능합니다.");
                mainImage.setImageResource(R.drawable.help_print);
                whiteCircleImage6.setImageResource(R.drawable.white_circle_full);
                break;
            case 6:
                textView1.setText(null);
                textView2.setText(null);
                textView3.setText(null);
                textView4.setText(null);

                finishText1.setText("이제 시작해보세요!");
                finishImage.setImageResource(R.drawable.next_circle);
                finishText2.setText("Let's GO Simulator");

                mainImage.setImageDrawable(null);
                whiteCircleImage1.setImageDrawable(null);
                whiteCircleImage2.setImageDrawable(null);
                whiteCircleImage3.setImageDrawable(null);
                whiteCircleImage4.setImageDrawable(null);
                whiteCircleImage5.setImageDrawable(null);
                whiteCircleImage6.setImageDrawable(null);
                break;
            default:
                break;
        }

        finishText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        finishImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return rootView;
    }
}
