package com.comakeit.inter_act.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

import com.comakeit.inter_act.R;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private RadioGroup mRadioGroup;
    private TextSwitcher mDescriptionTextSwitcher;
    private Button mSendButton;
    private EditText mDescriptionEditText, mRecipientEditText, mEventEditText, mSuggestionEditText;
    private Spinner mEventSpinner;
    private DrawerLayout mDrawerLayout;
    private String AUTH_TOKEN_PREF;
    private String AUTH_TOKEN;
    private Calendar mEventCalendar;
//    private Toolbar mToolbar;     //TODO Work on TOOLBAR
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
//        authenticateToken();

        //Navigation View Stuff
        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.menu_new_interaction).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch (item.getItemId()){
                            case R.id.menu_new_interaction:
                                Intent intent = new Intent(getApplicationContext(), TempFormActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.menu_main_form:
                                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent1);
                                finish();
                                break;
                            case R.id.menu_logout:
                                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(logoutIntent);
                                finish();
                                break;
                            case R.id.menu_received_interaction:
                                Snackbar.make(mDrawerLayout, getString(R.string.all_under_dev), Snackbar.LENGTH_LONG).show();
                                break;
                            case R.id.menu_sent_interaction:
                                Snackbar.make(mDrawerLayout, getString(R.string.all_under_dev), Snackbar.LENGTH_LONG).show();
                                break;
                        }

                        return true;
                    }
                }
        );



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
                    sendReport(TO, type, event, desc, suggestion);
                }
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void initViews(){
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        mSendButton = findViewById(R.id.main_send_button);
        mSendButton.setClickable(false);

        mDescriptionEditText = findViewById(R.id.descriptionEditText);
        mDescriptionEditText.setVisibility(View.GONE);  //Initially making the TextBox disappear
        mRecipientEditText = findViewById(R.id.recipientEditText);
        mEventEditText = findViewById(R.id.eventNameEditText);
        mEventSpinner = findViewById(R.id.eventSpinner);
        mSuggestionEditText = findViewById(R.id.suggestionEditText);

        mNavigationView = findViewById(R.id.main_navigation_view);
        mNavigationView.setCheckedItem(R.id.menu_main_form);
    }


    /**
     * Utility function for checking the app's token for accessing the REST services
     */
    public void authenticateToken(){
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.auth_token_pref), Context.MODE_PRIVATE);
        AUTH_TOKEN_PREF = getResources().getString(R.string.auth_token_pref);
        AUTH_TOKEN = sharedPreferences.getString(AUTH_TOKEN_PREF, "");

        if(AUTH_TOKEN.equals("")){
            AUTH_TOKEN = "PRVTOK1.6";
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(AUTH_TOKEN_PREF, AUTH_TOKEN).apply();
        }

        Toast.makeText(getApplicationContext(), "Authorization Token "+ AUTH_TOKEN, Toast.LENGTH_SHORT).show();
    }


    protected void sendReport(String TO, String iatype, String event, String description, String suggestion){
//        Log.i("Send Email","Trying to send email...");
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//        String recipient = "mailto:"+TO;
//        Log.i("Send email", recipient);
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
//        emailIntent.setType("message/rfc822");
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT,iatype+" regarding "+event);
//        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        String message = description + "\n Suggestion: " + suggestion;
        Interaction report = new Interaction(MainActivity.this);
        report.setToUser(TO);
        report.setAnonymous(false);
        report.setMessage(message);
        report.setIAType(iatype);
        report.setEventName(event);
        report.setEventCalendar(Calendar.getInstance());    //TODO Take time from TIME PICKER AND DATE PICKER fragment. PRIORITY: VERY HIGH

        if(report.validateReport(report))
            Log.i("Report Validation: ", "Report successfully validated");
        else
            Log.i("Report Validation: ", "Report NOT validated");
        /*
        try{
            startActivity(Intent.createChooser(emailIntent, "Send Interaction..."));    //TODO intent chooser as overlay. PRIORITY: VERY LOW
            finish();
//            Log.i("Send email", "Email sent!");
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }*/
    }

    public void setDate(Calendar calendar){
        mEventCalendar = calendar;
        Log.i("MainActvity:  ", "DATE" + mEventCalendar.get(Calendar.DAY_OF_MONTH) + mEventCalendar.get(Calendar.MONTH) + mEventCalendar.get(Calendar.YEAR));
    }
}
