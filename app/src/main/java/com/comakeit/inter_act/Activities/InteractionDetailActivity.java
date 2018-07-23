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

import com.comakeit.inter_act.GeneralUser;
import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.UserDetails;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InteractionDetailActivity extends AppCompatActivity {
    private static final String TAG = "InterAction Detail Act";
    private TextView mEventNameTextView, mContextTextView, mObservationTextView, mSuggestionTextView, userNameTextView, mEventDateTextView, mInteractionDateTextView;
    private LinearLayout mUpperLayout, mMiddleLayout, mBottomLayout;
    private Window window;
    private int FLAG = 0;       //0 for received and 1 for sent InterActions

    //TODO SET STATUS BAR COLOR ACCORDINGLY PRIORITY- HIGH

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction_detail);

        //Retrieve data from the parcel
        Interaction interaction = getIntent().getParcelableExtra("parcel_interaction");

        //Check IA type (sent or received) and set the flag
        FLAG = interaction.getFromUserId().equals(UserDetails.getUserID()) ? 1 : 0;

        //Check IA type and change STATUS BAR COLOR
        //Setting up flags to be able to change status bar color
        window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(interaction.getType() == 0)
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
        userNameTextView = findViewById(R.id.interaction_detail_from_text_view);
        mEventDateTextView = findViewById(R.id.interaction_detail_event_date_text_view);
        mInteractionDateTextView = findViewById(R.id.interaction_detail_ia_date_text_view);
        mUpperLayout = findViewById(R.id.interaction_detail_layout);
        mMiddleLayout = findViewById(R.id.interaction_detail_bottom_bar_linear_layout);
        mBottomLayout = findViewById(R.id.interaction_detail_detail_linear_layout);

        if (interaction.getType() == 0){
            mUpperLayout.setBackground(getDrawable(R.color.colorRoundedAmber));
            mMiddleLayout.setBackground(getDrawable(R.color.colorRoundedAmberDark));
            mBottomLayout.setBackground(getDrawable(R.color.colorRoundedAmberDark));
        }


        //SET ALL VIEWS
        mEventNameTextView.setText(interaction.getEventName());
        mContextTextView.setText(interaction.getContext());

        //Creating separate strings for OBS and SUGGESTION "IF" InterAction is a FEEDBACK.
        if(interaction.getType() == 0) {
            mObservationTextView.setText(interaction.getObservation());
            mSuggestionTextView.setText(interaction.getRecommendation());
        } else {
            mObservationTextView.setText(interaction.getObservation());
            mSuggestionTextView.setVisibility(View.GONE);
        }

        if(FLAG == 1){
            //noinspection SuspiciousMethodCalls
            String name = GeneralUser.sUserHashMap.get(interaction.getToUserId()).getFirstName() + " "
                    + GeneralUser.sUserHashMap.get(interaction.getToUserId()).getLastName();
            userNameTextView.setText(name);
        }
        else {
            if(interaction.isAnonymous())
                userNameTextView.setText(R.string.all_anonymous);
            else{
                String name = GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getFirstName() + " "
                        + GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getLastName();
                userNameTextView.setText(name);
            }
        }

        //Date objects for reading the date from Calendar objects
        Date eventDate, IADate;
        eventDate = interaction.getEventDateDate();
        Log.i("IA DETAILS", "EVENT DATE: " + eventDate.toString());
        IADate = interaction.getCreatedAtDate();
        Log.i("IA DETAILS", "IA DATE " + IADate.toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");   //TODO IMPLEMENT LOCALE CORRECTION
        mEventDateTextView.setText(simpleDateFormat.format(eventDate));
        mInteractionDateTextView.setText(simpleDateFormat.format(IADate));
    }
}
