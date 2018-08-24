package com.comakeit.inter_act.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.comakeit.inter_act.R;

public class SuccessAnimationActivity extends AppCompatActivity {

    private LottieAnimationView mLottieAnimationView;
    private ValueAnimator mValueAnimator;

    @Override
    protected void onResume(){
        super.onResume();
        mValueAnimator.resume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mValueAnimator.pause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_interaction_sent);
        mLottieAnimationView = findViewById(R.id.sent_lottie);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(getColor(android.R.color.transparent));
//        getWindow().setBackgroundDrawable(R.color.colorWhite);

        mValueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(2000);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLottieAnimationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class );
                startActivity(intent);
                finish();
            }
        });

        mValueAnimator.start();
        Toast.makeText(getApplicationContext(), "InterAction sent!", Toast.LENGTH_SHORT).show();
    }
}
