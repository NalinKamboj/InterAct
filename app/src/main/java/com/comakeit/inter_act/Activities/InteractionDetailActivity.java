package com.comakeit.inter_act.Activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InteractionDetailActivity extends AppCompatActivity {
    private TextView mEventNameTextView, mContextTextView, mObservationTextView, mSuggestionTextView, mFromUserTextView, mEventDateTextView, mInteractionDateTextView;
    private LinearLayout mUpperLayout, mMiddleLayout, mBottomLayout;
    private Window window;

    //TODO SET STATUS BAR COLOR ACCORDINGLY PRIORITY- HIGH

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction_detail);

        //Retrieve data from the parcel
        Interaction interaction = (Interaction) getIntent().getParcelableExtra("parcel_interaction");

        //Check IA type and change STATUS BAR COLOR
            //Setting up flags to be able to change status bar color
        window = this.getWindow();
            // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(interaction.getIAType() == 0)
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedAmberDark));      //Changing status bar color
        else
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedGreenDark));      //Changing status bar color

        //Initialize all view objects
        initViews(interaction);
    }

    private void initViews(Interaction interaction) {
        mEventNameTextView = findViewById(R.id.interaction_detail_event_name_text_view);
        mContextTextView = findViewById(R.id.interaction_detail_context_text_view);
        mObservationTextView = findViewById(R.id.interaction_detail_description_text_view);
        mSuggestionTextView = findViewById(R.id.interaction_detail_suggestion_text_view);
        mFromUserTextView = findViewById(R.id.interaction_detail_from_text_view);
        mEventDateTextView = findViewById(R.id.interaction_detail_event_date_text_view);
        mInteractionDateTextView = findViewById(R.id.interaction_detail_ia_date_text_view);
        mUpperLayout = findViewById(R.id.interaction_detail_layout);
        mMiddleLayout = findViewById(R.id.interaction_detail_bottom_bar_linear_layout);
        mBottomLayout = findViewById(R.id.interaction_detail_detail_linear_layout);

        if (interaction.getIAType() == 0){
            mUpperLayout.setBackground(getDrawable(R.color.colorRoundedAmber));
            mMiddleLayout.setBackground(getDrawable(R.color.colorRoundedAmberDark));
            mBottomLayout.setBackground(getDrawable(R.color.colorRoundedAmberDark));
        }


        //SET ALL VIEWS

        mEventNameTextView.setText(interaction.getEventName());
        mContextTextView.setText(interaction.getContext());

        //Creating separate strings for OBS and SUGGESTION "IF" InterAction is a FEEDBACK.
        if(interaction.getIAType() == 0) {
            String parts[] = interaction.getDescription().split("Suggestion:");
            Log.i("IA DETAIL ACTIVITY", "Description: " + interaction.getDescription());
            mObservationTextView.setText(parts[0].trim());
            mSuggestionTextView.setText(parts[1].trim());
        } else {
            mObservationTextView.setText(interaction.getDescription().trim());
            mSuggestionTextView.setVisibility(View.GONE);
        }

        if(interaction.isAnonymous())
            mFromUserTextView.setText(R.string.all_anonymous);
        else
            mFromUserTextView.setText(interaction.getFromUserEmail().toUpperCase());

        //Date objects for reading the date from Calendar objects
        Date eventDate, IADate;
        eventDate = interaction.getEventCalendar().getTime();
        Log.i("IA DETAILS", "EVENT DATE: " + eventDate.toString());
        IADate = interaction.getIACalendar().getTime();
        Log.i("IA DETAILS", "IA DATE " + IADate.toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");   //TODO IMPLEMENT LOCALE CORRECTION
        mEventDateTextView.setText(simpleDateFormat.format(eventDate));
        mInteractionDateTextView.setText(simpleDateFormat.format(IADate));
    }
}
