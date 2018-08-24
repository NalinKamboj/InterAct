package com.comakeit.inter_act.activities;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.comakeit.inter_act.R;

public class TestingActivity extends AppCompatActivity {

    private LottieAnimationView mLottieStarView1, mLottieStarView2, mLottieStarView3;
    private LinearLayout starLayout1, starLayout2, starLayout3;
    private ValueAnimator mValueAnimator1, mValueAnimator2, mValueAnimator3;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);

        ///Get lottie animation views
        mLottieStarView1 = findViewById(R.id.rating_lottie_star_1);
        mLottieStarView2 = findViewById(R.id.rating_lottie_star_2);
        mLottieStarView3 = findViewById(R.id.rating_lottie_star_3);

        //Get enclosing lin-layouts
        starLayout1 = findViewById(R.id.rating_star_layout_1);
        starLayout2 = findViewById(R.id.rating_star_layout_2);
        starLayout3 = findViewById(R.id.rating_star_layout_3);

        //Create animators for all Lottie Animations
        mValueAnimator1 = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
        mValueAnimator2 = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);
        mValueAnimator3 = ValueAnimator.ofFloat(0f, 1f).setDuration(1500);

        //Create animation update methods
        mValueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  //Star 1
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLottieStarView1.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });
        mValueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  //Star 2
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLottieStarView2.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });
        mValueAnimator3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  //Star 3
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLottieStarView3.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

//        set listeners on each star view
        starLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pausing is essential to set progress to zero in case an animation is already under way while being clicked
                mValueAnimator2.pause();
                mValueAnimator3.pause();
                mLottieStarView2.setProgress(0f);
                mLottieStarView3.setProgress(0f);

                if(mLottieStarView1.getProgress() != 1f)
                    mValueAnimator1.start();
            }
        });
        starLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLottieStarView2.getProgress() != 1f)
                    mValueAnimator2.start();
                if(mLottieStarView1.getProgress() != 1f)
                    mValueAnimator1.start();

                mValueAnimator3.pause();
                mLottieStarView3.setProgress(0f);
            }
        });
        starLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mLottieStarView1.getProgress() != 1f)
                    mValueAnimator1.start();
                if(mLottieStarView2.getProgress() != 1f)
                    mValueAnimator2.start();
                if(mLottieStarView3.getProgress() != 1f)
                    mValueAnimator3.start();
            }
        });
    }
}
