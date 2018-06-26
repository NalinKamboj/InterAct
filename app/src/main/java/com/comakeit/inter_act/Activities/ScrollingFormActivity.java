package com.comakeit.inter_act.Activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.comakeit.inter_act.DateTimePickerFragment;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.sql.DatabaseHelper;

import java.util.Calendar;

public class ScrollingFormActivity extends AppCompatActivity {
    private Spinner mEventSpinner;
    private EditText mEventEditText, mDescriptionEditText, mSuggestionEditText, mEmailEditText;
    private TextSwitcher mAnonymousTextSwitcher;
    private ToggleButton mInteractionToggleButton;
    private FloatingActionButton mFloatingActionButton;
    private TextInputLayout mInteractionTextInputLayout;
    private SwitchCompat mAnonymousSwitchCompat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_form);

        initViews();
    }

    private void initViews(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialize all widgets and elements
        mAnonymousSwitchCompat = findViewById(R.id.new_interaction_anonymous_switch);
        mAnonymousTextSwitcher = findViewById(R.id.new_interaction_anonymous_text_switcher);
        mEventSpinner = findViewById(R.id.new_interaction_event_spinner);
        mEventEditText = findViewById(R.id.new_interaction_event_edit_text);
        mFloatingActionButton = findViewById(R.id.new_interaction_fab);
        mEmailEditText = findViewById(R.id.new_interaction_email_edit_text);
        mDescriptionEditText = findViewById(R.id.new_interaction_description_edit_text);
        mInteractionToggleButton = findViewById(R.id.new_interaction_interaction_toggle_button);
        mDescriptionEditText = findViewById(R.id.new_interaction_description_edit_text);
        mSuggestionEditText = findViewById(R.id.new_interaction_suggestion_edit_text);
        mInteractionTextInputLayout = findViewById(R.id.new_interaction_description_input_layout);
        mSuggestionEditText.setVisibility(View.GONE);

        //Create the Date and Time picker fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.new_interaction_date_time_picker_frame, new DateTimePickerFragment());
        fragmentTransaction.commit();

        //Initializing the Toggle Button
        mInteractionToggleButton.setChecked(false);
        mInteractionToggleButton.setHapticFeedbackEnabled(true);
        mInteractionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mInteractionToggleButton.isChecked()){
                    mSuggestionEditText.setVisibility(View.VISIBLE);
                    mInteractionTextInputLayout.setHint(getResources().getString(R.string.all_appreciation));
                } else{
                    mInteractionTextInputLayout.setHint(getResources().getString(R.string.all_feedback));
                    mSuggestionEditText.setVisibility(View.GONE);
                }
            }
        });

        //TextSwitcher for Anonymous field
        mAnonymousTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                TextView textView = new TextView(ScrollingFormActivity.this);
                textView.setTextColor(getColor(R.color.colorSecondaryDark));
                textView.setGravity(Gravity.CENTER);
                textView.setPadding(5,5,5,5);
                textView.setTextSize(18);
                return textView;
            }
        });
        mAnonymousTextSwitcher.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        mAnonymousTextSwitcher.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);


        //Listener for Anonymous Switch
        mAnonymousTextSwitcher.setText(getResources().getString(R.string.all_not_anonymous));
        mAnonymousSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mAnonymousSwitchCompat.isChecked())
                    mAnonymousTextSwitcher.setText(getResources().getString(R.string.all_anonymous));
                else
                    mAnonymousTextSwitcher.setText(getResources().getString(R.string.all_not_anonymous));
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mDescriptionEditText.getText().toString().trim().length()==0)
                    Toast.makeText(ScrollingFormActivity.this, "Feedback/Appreciation can't be left blank", Toast.LENGTH_SHORT).show();
                else if(mEmailEditText.getText().toString().trim().length()==0)
                    Toast.makeText(ScrollingFormActivity.this, "Please enter the RECIPIENT", Toast.LENGTH_SHORT).show();
                else if(mInteractionToggleButton.isChecked() && mSuggestionEditText.getText().toString().trim().equals(""))
                    Toast.makeText(ScrollingFormActivity.this, "Suggestion can't be left blank", Toast.LENGTH_SHORT).show();
                else{
//                    mSendButton.setClickable(true);       TODO Make the button NOT CLICKABLE until all fields are filled.
                    String suggestion = "";
                    String TO_EMAIL = mEmailEditText.getText().toString().toUpperCase();
                    String type = mInteractionToggleButton.isChecked()?"AP":"FB";
                    if(type.equals("FB")){
                        suggestion = mSuggestionEditText.getText().toString();
                    }
                    String event;
                    if(mEventEditText.getVisibility()==View.VISIBLE)
                        event = "\"" + mEventEditText.getText().toString() + "\"";
                    else
                        event = "\"" + mEventSpinner.getSelectedItem().toString() + "\"";
                    String desc = mDescriptionEditText.getText().toString() + "\n Suggestion: " + suggestion;
                    sendReport(TO_EMAIL, type, event, desc, mAnonymousSwitchCompat.isChecked());
                }
            }
        });


        //Adapter for Spinner
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter.createFromResource(this, R.array.event_types, android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEventSpinner.setSelection(0);
        mEventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if(adapterView.getItemAtPosition(pos).toString().equals("Custom Event")){
                    mEventEditText.setVisibility(View.VISIBLE);
                }
                else
                    mEventEditText.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mEventSpinner.setAdapter(eventAdapter);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    protected void sendReport(String toEmail, String iatype, String event, String description, boolean isAnonymous){
//        String message = description + "\n Suggestion: " + suggestion;
        Interaction report = new Interaction(ScrollingFormActivity.this);

        //Create a report
        report.setToUser(toEmail);
        report.setAnonymous(isAnonymous);
        report.setMessage(description);
        report.setIAType(iatype);
        report.setEventName(event);
        report.setEventCalendar(Calendar.getInstance());    //TODO Take time from TIME PICKER AND DATE PICKER fragment. PRIORITY: VERY HIGH

        if(report.validateReport(report, getApplicationContext()))
            Log.i("Report Validation: ", "Report successfully validated");
        else
            Log.i("Report Validation: ", "Report NOT validated");

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        databaseHelper.addTnterAction(report);
//        showDialog();
    }
}
