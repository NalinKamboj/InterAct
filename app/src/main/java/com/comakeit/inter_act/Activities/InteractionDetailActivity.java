package com.comakeit.inter_act.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;

public class InteractionDetailActivity extends AppCompatActivity {
    private TextView mEventNameTextView, mContextTextView, mObservationTextView, mSuggestionTextView, mFromUserTextView, mEventDateTextView, mInteractionDateTextView;


    //TODO SET STATUS BAR COLOR ACCORDINGLY PRIORITY- HIGH

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction_detail);

        //Retrieve data from the parcel
        Interaction interaction = (Interaction) getIntent().getParcelableExtra("parcel_interaction");
    }
}
