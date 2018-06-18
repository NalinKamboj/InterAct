package com.comakeit.inter_act;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import org.json.JSONObject;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RadioGroup mRadioGroup;
    private TextSwitcher mDescriptionTextSwitcher;
    private Button mSendButton;
    private EditText mDescriptionEditText, mRecipientEditText, mEventEditText, mSuggestionEditText;
    private Spinner mEventSpinner;
    private String AUTH_TOKEN_PREF;
    private Calendar mEventCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        final String eventName = "";


        mSendButton = findViewById(R.id.main_send_button);
        mSendButton.setClickable(false);

        mDescriptionEditText = findViewById(R.id.descriptionEditText);
        mDescriptionEditText.setVisibility(View.GONE);  //Initially making the TextBox disappear
        mRecipientEditText = findViewById(R.id.recipientEditText);
        mEventEditText = findViewById(R.id.eventNameEditText);
        mEventSpinner = findViewById(R.id.eventSpinner);
        mSuggestionEditText = findViewById(R.id.suggestionEditText);

        authenticateToken();

        //Adapter for Spinner
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter.createFromResource(this, R.array.event_types, android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        mRadioGroup = findViewById(R.id.iaTypeRadioGroup);
        mRadioGroup.clearCheck();

        mDescriptionTextSwitcher = findViewById(R.id.descriptionTextSwitcher);
        mDescriptionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                                                @Override
                                                public View makeView() {
                                                    TextView textView = new TextView(MainActivity.this);
                                                    textView.setGravity(Gravity.TOP);
                                                    textView.setPadding(5,5,5,5);
                                                    textView.setTextSize(20);
                                                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                                                    return textView;
                                                }
                                            });
                mDescriptionTextSwitcher.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        mDescriptionTextSwitcher.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null){
                    mDescriptionTextSwitcher.setText(rb.getText());
                    mDescriptionEditText.setVisibility(View.VISIBLE);
                    if(rb.getText().toString().equals("Feedback")){
                        mSuggestionEditText.setVisibility(View.VISIBLE);
                    }else
                        mSuggestionEditText.setVisibility(View.GONE);
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRadioGroup.getCheckedRadioButtonId() == -1)
                    Toast.makeText(MainActivity.this, "Please select FEEDBACK or APPRECIATION.", Toast.LENGTH_SHORT).show();
                else if(mDescriptionEditText.getText().toString().trim().length()==0)
                    Toast.makeText(MainActivity.this, "Feedback/Appreciation can't be left blank", Toast.LENGTH_SHORT).show();
                else if(mRecipientEditText.getText().toString().trim().length()==0)
                    Toast.makeText(MainActivity.this, "Please enter the RECIPIENT", Toast.LENGTH_SHORT).show();
                else{
//                    mSendButton.setClickable(true);       TODO Make the button NOT CLICKABLE until all fields are filled.
                    String suggestion = "";
                    String TO = mRecipientEditText.getText().toString();
                    RadioButton rb = findViewById(mRadioGroup.getCheckedRadioButtonId());
                    String type = rb.getText().toString();
                    if(type.equals("Feedback") && mSuggestionEditText.getText().toString().trim().length() != 0){
                        suggestion = mSuggestionEditText.getText().toString();
                    }
                    String event;
                    if(mEventEditText.getVisibility()==View.VISIBLE)
                        event = "\"" + mEventEditText.getText().toString() + "\"";
                    else
                        event = "\"" + mEventSpinner.getSelectedItem().toString() + "\"";
//                    String event = "\"" + eventName + "\"";
                    String desc = mDescriptionEditText.getText().toString();
                    sendEmail(TO, type, event, desc, suggestion);
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    /**
     * Utility function for checking the app's token for accessing the REST services
     */
    public void authenticateToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.auth_token_pref), Context.MODE_PRIVATE);
        AUTH_TOKEN_PREF = getResources().getString(R.string.auth_token_pref);
        String AUTH_TOKEN = sharedPreferences.getString(AUTH_TOKEN_PREF, "");

        if(AUTH_TOKEN.equals("")){
            AUTH_TOKEN = "PRVTOK1.6";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AUTH_TOKEN_PREF, AUTH_TOKEN).apply();
        }

        Toast.makeText(getApplicationContext(), "Authorization Token "+ AUTH_TOKEN, Toast.LENGTH_SHORT).show();
    }


    protected void sendEmail(String TO, String iatype, String event, String description, String suggestion){
        Log.i("Send Email","Trying to send email...");
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String message = description + "\n Suggestion: " + suggestion;
//        String recipient = "mailto:"+TO;
//        Log.i("Send email", recipient);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,iatype+" regarding "+event);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        try{
            startActivity(Intent.createChooser(emailIntent, "Send Interaction..."));        //TODO Intent chooser doesn't open as overlay, YET
            finish();
//            Log.i("Send email", "Email sent!");
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }

    private void createReport(String to, String IA_Type, String event, String description, String suggestion){
        JSONObject report = new JSONObject();
        Calendar calendar = Calendar.getInstance(); //Local var for IA Timestamp
        /*
        try{
            report.put("From", from);   //Should be generated within app
            report.put("To", to);
            report.put("Event Name", event);
            report.put("Event Date", eventDate);    //TODO Timestamp?
            report.put("Event Time", eventTime);
            report.put("IsAnonymous", anonymous);
            report.put("IA ID", IA_ID);
            report.put("IA Type", IA_Type);
            report.put("IA Time", IA_Time);
            report.put("Description", description);
            report.put("Suggestion", suggestion);
        } catch (JSONException e){
            //TODO Auto-generated catch block
            e.printStackTrace();
        }*/
    }

    public void setDate(Calendar calendar){
        mEventCalendar = calendar;
        Log.i("MainActvity:  ", "DATE" + mEventCalendar.get(Calendar.DAY_OF_MONTH) + mEventCalendar.get(Calendar.MONTH) + mEventCalendar.get(Calendar.YEAR));
    }
}
