package com.comakeit.inter_act.Activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.SeekBar;
import android.widget.TextView;

import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.Utilities;

public class ActionFormActivity extends AppCompatActivity {

    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";

    View rootView;
    private TextView mEventNameTextView, mEventDateTextView, mProgressTextView, mReviewerTextView;
    private SeekBar mProgressSeekbar;

    private int revealX;
    private int revealY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_form);
        final Intent intent = getIntent();

        rootView = findViewById(R.id.action_form_root_layout);

        if(savedInstanceState == null && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)){

            rootView.setVisibility(View.INVISIBLE);
            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();

            if(viewTreeObserver.isAlive()){
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity(revealX, revealY);
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }

        } else
            rootView.setVisibility(View.VISIBLE);

        //Retrieve data from parcel
        Interaction interaction = getIntent().getParcelableExtra("parcel_interaction");
        initViews(interaction);
    }

    private void initViews(Interaction interaction){
        mEventNameTextView = findViewById(R.id.action_event_name_text_view);
        mEventDateTextView = findViewById(R.id.action_event_date_text_view);
        mProgressTextView = findViewById(R.id.action_progress_text_view);
        mReviewerTextView = findViewById(R.id.action_reviewer_text_view);
        mProgressSeekbar = findViewById(R.id.action_seek_bar);

        mEventNameTextView.setText(Utilities.toCamelCase(interaction.getEventName()));
        mEventDateTextView.setText(String.format("on %s", interaction.getEventDate()));

        //Set seek bar values and listners
//        mProgressSeekbar.incrementProgressBy(1);
        mProgressSeekbar.setProgress(0);
        mProgressSeekbar.setMax(4);
        mProgressSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0:
                        mProgressTextView.setText(getString(R.string.progress_not_started));
                        mProgressTextView.setTextColor(getColor(android.R.color.black));
                        break;
                    case 1:
                        mProgressTextView.setText(getString(R.string.progress_started));
                        mProgressTextView.setTextColor(getColor(R.color.colorRoundedBabyPinkDark));
                        break;
                    case 2:
                        mProgressTextView.setText(getString(R.string.progress_ongoing));
                        mProgressTextView.setTextColor(getColor(R.color.colorRoundedAmberDark));
                        break;
                    case 3:
                        mProgressTextView.setText(getString(R.string.progress_finished));
                        mProgressTextView.setTextColor(getColor(R.color.colorRoundedGreenDark));
                        break;
                    case 4:
                        mProgressTextView.setText(getString(R.string.progress_reviewed));
                        mProgressTextView.setTextColor(getColor(R.color.colorRoundedPurpleDark));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    protected void revealActivity(int x, int y){
        float finalRadius = (float) (Math.max(rootView.getWidth(), rootView.getHeight()) * 1.1);

        //Create the animator for this view... (start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootView, x, y, 0, finalRadius);
        circularReveal.setDuration(400);
        circularReveal.setInterpolator(new AccelerateInterpolator());

        //make view visible and start animation
        rootView.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

}
