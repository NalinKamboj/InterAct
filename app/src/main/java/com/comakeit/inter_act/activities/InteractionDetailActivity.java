package com.comakeit.inter_act.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.comakeit.inter_act.Action;
import com.comakeit.inter_act.GeneralUser;
import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.Utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InteractionDetailActivity extends AppCompatActivity {
    private static final String TAG = "InterAction Detail Act";
    private TextView mEventNameTextView, mContextTextView, mObservationTextView, mSuggestionTextView, userNameTextView, mEventDateTextView, mInteractionDateTextView, mActionTitle;
    private LinearLayout mUpperLayout, mBottomLayout;
    private Window window;
    private Button mActionButton;
    private LinearLayout mActionDescLayout, mActionStatusLayout, mActionsLayout;
    private View actionDivider;
    private int FLAG = 0;       //0 for received and 1 for sent InterActions

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interaction_detail);

        //Retrieve data from the parcel
        Interaction parcel_interaction = getIntent().getParcelableExtra("parcel_interaction");

        //Check IA type (sent or received) and set the flag
        FLAG = parcel_interaction.getFromUserId().equals(UserDetails.getUserID()) ? 1 : 0;

        //Check IA type and change STATUS BAR COLOR
        //Setting up flags to be able to change status bar color
        window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if(parcel_interaction.getType() == 0)
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedPurpleDark));      //Changing status bar color
        else
            window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedGreenDark));      //Changing status bar color

        //Initialize all view objects
        initViews(parcel_interaction);
        Log.e(TAG, "INTERACTION ID - " + parcel_interaction.getInteractionID());
    }

    private void initViews(final Interaction interaction) {
        mEventNameTextView = findViewById(R.id.interaction_detail_event_name_text_view);
        mContextTextView = findViewById(R.id.interaction_detail_context_text_view);
        mObservationTextView = findViewById(R.id.interaction_detail_description_text_view);
        mSuggestionTextView = findViewById(R.id.interaction_detail_suggestion_text_view);
        userNameTextView = findViewById(R.id.interaction_detail_from_text_view);
        mEventDateTextView = findViewById(R.id.interaction_detail_event_date_text_view);
        mInteractionDateTextView = findViewById(R.id.interaction_detail_ia_date_text_view);
//        mUpperLayout = findViewById(R.id.interaction_detail_layout);
        mUpperLayout = findViewById(R.id.interaction_detail_top_bar_linear_layout);
        mBottomLayout = findViewById(R.id.interaction_detail_detail_linear_layout);
        mActionButton = findViewById(R.id.add_action_button);
        actionDivider = findViewById(R.id.interaction_detail_action_divider);
        mActionTitle = findViewById(R.id.interaction_detail_action_title);
        mActionsLayout = findViewById(R.id.interaction_detail_actions_layout);
        mActionDescLayout = findViewById(R.id.interaction_detail_action_desc_layout);
        mActionStatusLayout = findViewById(R.id.interaction_detail_action_status_layout);

        //Display actions taken by Receiver
        if(interaction.getActionList().size() > 0){
            //First make the divider, view and title visible
            actionDivider.setVisibility(View.VISIBLE);
            mActionTitle.setVisibility(View.VISIBLE);
            mActionsLayout.setVisibility(View.VISIBLE);

            final int size = interaction.getActionList().size();

            //Create all textviews
            for(int i = 0; i<size; i++){
                Action action = (Action) interaction.getActionList().get(i);
                final TextView actionView = new TextView(this);
                final TextView actionProgress = new TextView(this);
                final String desc = Utilities.toCamelCase(action.getDescription());
                final String progress = Utilities.getProgress(action.getProgress());
                actionView.setText(desc);
                actionView.setTextColor(getColor(R.color.colorGreyDark));
                actionView.setTextSize(20);
                actionView.setPadding(5, 5, 5, 5);
                actionView.setTypeface(null, Typeface.BOLD);

                actionProgress.setGravity(Gravity.RIGHT);
                actionProgress.setText(progress);
                actionProgress.setTextColor(getColor(R.color.colorGreyDark));
                actionProgress.setTextSize(20);
                actionProgress.setPadding(5, 5, 5, 5);
                actionProgress.setTypeface(null, Typeface.BOLD);
                //Finally, change the color of progress text...
                switch (action.getProgress()){
                    case 0:
                        actionProgress.setTextColor(getColor(android.R.color.black));
                        break;
                    case 1:
                        actionProgress.setTextColor(getColor(R.color.colorRoundedBabyPinkDark));
                        break;
                    case 2:
                        actionProgress.setTextColor(getColor(R.color.colorRoundedAmberDark));
                        break;
                    case 3:
                        actionProgress.setTextColor(getColor(R.color.colorRoundedGreenDark));
                        break;
                    case 4:
                        actionProgress.setTextColor(getColor(R.color.colorRoundedPurpleDark));
                        break;
                }


                mActionDescLayout.addView(actionView);
                mActionStatusLayout.addView(actionProgress);
            }

            Log.e(TAG, "NUMBER OF ACTIONS - " + interaction.getActionList().size());
        }

        //Set background color of layout after checking interaction type
        if (interaction.getType() == 0){
            mUpperLayout.setBackground(getDrawable(R.color.colorRoundedPurpleDark));
            mSuggestionTextView.setVisibility(View.VISIBLE);
        } else
            mUpperLayout.setBackground(getDrawable(R.color.colorRoundedGreenDark));


        //SET ALL VIEWS
        mEventNameTextView.setText(interaction.getEventName());
        mContextTextView.setText(interaction.getContext());

        mObservationTextView.setText(interaction.getObservation());
        //Removing Suggestions if InterAction is not Feedback
        if(interaction.getType() == 0)
            mSuggestionTextView.setText(interaction.getRecommendation());
        else
            mSuggestionTextView.setVisibility(View.GONE);

        //If Interaction is SENT
        if(FLAG == 1){
            //noinspection SuspiciousMethodCalls
            String name = GeneralUser.sUserHashMap.get(interaction.getToUserId()).getFirstName() + " "
                    + GeneralUser.sUserHashMap.get(interaction.getToUserId()).getLastName();
            userNameTextView.setText(name);
            mActionButton.setVisibility(View.GONE);
        }
        else {
            if(interaction.isAnonymous())
                userNameTextView.setText(R.string.all_anonymous);
            else{
                String name = "From " + GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getFirstName() + " "
                        + GeneralUser.sUserHashMap.get(interaction.getFromUserId()).getLastName();
                userNameTextView.setText(name);
            }
        }

        //Add listener on button to create a new action
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ActionFormActivity.class);
//                startActivity(intent);
                presentActivity(view, interaction);
            }
        });

        //Date objects for reading the date from Calendar objects
        Date eventDate, IADate;
        eventDate = interaction.getEventDateDate();
        Log.i("IA DETAILS", "EVENT DATE: " + eventDate.toString());
        IADate = interaction.getCreatedAtDate();
        Log.i("IA DETAILS", "IA DATE " + IADate.toString());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");   //TODO IMPLEMENT LOCALE CORRECTION
        mEventDateTextView.setText(String.format("on %s", simpleDateFormat.format(eventDate)));
        mInteractionDateTextView.setText(simpleDateFormat.format(IADate));
    }

    private void presentActivity(View view, Interaction interaction){
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "circular-transition");

        //Get coordinates of button to set begin point of animation
        int revealX = (int) (view.getX() + view.getWidth()/2);
        int revealY = (int) (view.getY() + view.getHeight()/2);

        //Pass coordinates of button to activity
        Intent intent = new Intent(this, ActionFormActivity.class);
        intent.putExtra(ActionFormActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(ActionFormActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra("parcel_interaction", interaction);
        intent.putExtra("user_name", userNameTextView.getText());

        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
        finish();
    }
}
