package com.comakeit.inter_act.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.comakeit.inter_act.PreferenceHelper;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.SliderAdapter;

public class OnboardingActivity extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotsLayout;
    private SliderAdapter mSliderAdapter;
    private TextView[] mDots;
    private Button mBackButton;
    private Button mNextButton;
    private RelativeLayout mRelativeLayout;
    SharedPreferences mSharedPreferences;
    private Window mWindow;
    private int mCurrentPage;


    //TODO add onResume and onPause and stop/resume background animation to manage resources efficiently
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        mRelativeLayout = findViewById(R.id.onboarding_relative_layout);

//        Window window = getWindow();
//        WindowManager.LayoutParams winParams = window.getAttributes();
//        winParams.flags &= ~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        window.setAttributes(winParams);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        if(hasSoftKeys(getWindowManager()) > 0){
            mRelativeLayout.setPadding(0,0,0, hasSoftKeys(getWindowManager()));
        }

        //Animate background
        AnimationDrawable animationDrawable = (AnimationDrawable) mRelativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(3000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        mSharedPreferences = getSharedPreferences("TaskBotPreferences", MODE_PRIVATE);
        mSlideViewPager = findViewById(R.id.slideViewPager);
        mDotsLayout = findViewById(R.id.dotsLayout);
        mNextButton = findViewById(R.id.next_button);
        mBackButton = findViewById(R.id.back_button);

        mSliderAdapter = new SliderAdapter(this);
        mSlideViewPager.setAdapter(mSliderAdapter);

        addDotsIndicator(0);
        mSlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int i1){
                //ArgbEvaluator evaluator = new ArgbEvaluator();
//                int colorUpdate = (Integer) evaluator.evaluate(positionOffset, colorList[position],
//                        colorList[position == 2 ? position : position + 1]);
//                mSlideViewPager.setBackgroundColor(colorUpdate);
            }

            @Override
            public void onPageSelected(int position){
                addDotsIndicator(position);
                mCurrentPage = position;
                if(position == 0){
//                    mSlideViewPager.setBackgroundColor(color1);
                    mNextButton.setEnabled(true);
                    mBackButton.setEnabled(false);
                    mBackButton.setVisibility(View.INVISIBLE);
                    mNextButton.setText(R.string.button_next_text);

                }else if ( position == mDots.length-1){
//                    mSlideViewPager.setBackgroundColor(color2);
                    mNextButton.setEnabled(true);
                    mBackButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);
                    mBackButton.setText(R.string.button_back_text);
                    mNextButton.setText(R.string.all_finish);
                } else{
//                    mSlideViewPager.setBackgroundColor(color3);
                    mNextButton.setEnabled(true);
                    mBackButton.setEnabled(true);
                    mBackButton.setVisibility(View.VISIBLE);
                    mBackButton.setText(R.string.button_back_text);
                    mNextButton.setText(R.string.button_next_text);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state){

            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlideViewPager.setCurrentItem(mCurrentPage - 1);
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCurrentPage!=2)
                    mSlideViewPager.setCurrentItem(mCurrentPage+1);
                else
                    finishOnboarding();
            }
        });
    }

    public int hasSoftKeys(WindowManager windowManager){
        Display display = getWindowManager().getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        display.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        //We dont need width for now...

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        return (realHeight - displayHeight);
    }

    public void addDotsIndicator(int position){
        mDots = new TextView[mSliderAdapter.getCount()];
        mDotsLayout.removeAllViews();
        final int colorGray = ContextCompat.getColor(this, R.color.colorWhite);
        final int colorBlack = ContextCompat.getColor(this, android.R.color.black);
        for(int i = 0; i<mDots.length; i++){
            mDots[i] = new TextView(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mDots[i].setText(Html.fromHtml("&#8226;", Html.FROM_HTML_MODE_LEGACY));
            }
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(colorGray);

            mDotsLayout.addView(mDots[i]);
        }

        if (mDots.length > 0){
            mDots[position].setTextColor(colorBlack);
        }
    }


    public void finishOnboarding(){
        PreferenceHelper.getInstance().setPreference(this, PreferenceHelper.PREFERENCE_ONBOARDING, true);
        Intent main = new Intent(this, LoginActivity.class);
        startActivity(main);
        finish();
    }
}
