package com.comakeit.inter_act.Activities;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.sql.DatabaseHelper;

import java.util.Calendar;
import java.util.Objects;

public class ScrollingFormActivity extends AppCompatActivity {
    private Spinner mEventSpinner;
    private EditText mEventEditText, mDescriptionEditText, mSuggestionEditText, mEmailEditText, mContextEditText;
    private TextSwitcher mAnonymousTextSwitcher;
    private ToggleButton mInteractionToggleButton;
    private FloatingActionButton mFloatingActionButton;
    private TextInputLayout mInteractionTextInputLayout;
    private SwitchCompat mAnonymousSwitchCompat;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Menu appBarMenu;
    private Window window;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_form);

        //Setting up flags to be able to change status bar color
        window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedAmberDark));      //Changing status bar color

        initNavigationDrawer();
        initViews();
    }

    private void initNavigationDrawer(){
        mNavigationView = findViewById(R.id.scrolling_navigation_view);
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        Toolbar toolbar = findViewById(R.id.new_interaction_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        String parts[] = UserDetails.getUserEmail().split("@");
        String welcome = "Welcome " + parts[0];
        View headerView = mNavigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.navigation_view_header_text_view);
        navUserName.setText(welcome);

        AppBarLayout appBarLayout = findViewById(R.id.new_interaction_app_bar_layout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(scrollRange == -1)
                    scrollRange = appBarLayout.getTotalScrollRange();
                if(scrollRange + verticalOffset == 0) {
                    isShow = true;
                    showOption(R.id.action_send);
                } else if (isShow) {
                    isShow = false;
                    hideOption(R.id.action_send);
                }
            }
        });

        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.menu_new_interaction).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch (item.getItemId()){
                            case R.id.menu_new_interaction:
                                Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.menu_logout:
                                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(logoutIntent);
                                finish();
                                break;
                            case R.id.menu_received_interaction:
//                                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
//                                databaseHelper.tempFunc(getApplicationContext());
//                                Snackbar.make(mDrawerLayout, getString(R.string.all_under_dev), Snackbar.LENGTH_LONG).show();
                                Intent receivedInteractionIntent = new Intent(getApplicationContext(), ReceivedInteractionActivity.class);
                                startActivity(receivedInteractionIntent);
                                break;
                            case R.id.menu_sent_interaction:
                                Snackbar.make(mDrawerLayout, getString(R.string.all_under_dev), Snackbar.LENGTH_LONG).show();
                                break;
                        }

                        return true;
                    }
                }
        );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate menu. This adds items to Action Bar if it is present
        appBarMenu = menu;
        getMenuInflater().inflate(R.menu.menu_scrolling_form, menu);
        hideOption(R.id.action_send);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_send) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void hideOption(int id) {
        MenuItem item = appBarMenu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id) {
        MenuItem item = appBarMenu.findItem(id);
        item.setVisible(true);
    }

    private void initViews(){

        //Initialize all widgets and elements
        mContextEditText = findViewById(R.id.new_interaction_context_edit_text);
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
        mSuggestionEditText.setVisibility(View.VISIBLE);


        //Create the Date and Time picker fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.new_interaction_date_time_picker_frame, new DateTimePickerFragment());
        fragmentTransaction.commit();

        //Initializing the Toggle Button
        mInteractionToggleButton.setChecked(false);
        mInteractionToggleButton.setHapticFeedbackEnabled(true);
        mInteractionToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            TransitionDrawable mTransition = (TransitionDrawable) mInteractionToggleButton.getBackground();

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mInteractionToggleButton.isChecked()){
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedGreenDark));      //Changing status bar color
                    mTransition.startTransition(250);       //Start transition here because button is initially NOT CHECKED.
                    mInteractionTextInputLayout.setHint(getResources().getString(R.string.all_appreciation));
                    mSuggestionEditText.setVisibility(View.GONE);
                } else{
                    window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedAmberDark));      //Changing status bar color
                    mTransition.reverseTransition(250);
                    mSuggestionEditText.setVisibility(View.VISIBLE);
                    mInteractionTextInputLayout.setHint(getResources().getString(R.string.all_feedback));
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
                if(mDescriptionEditText.getText().toString().trim().length() < 8)
                    Toast.makeText(ScrollingFormActivity.this, "Feedback/Appreciation can't be left blank or less than 8 characters"
                            , Toast.LENGTH_SHORT).show();
                else if(mEmailEditText.getText().toString().trim().length() < 3)
                    Toast.makeText(ScrollingFormActivity.this, "Please enter a valid EMAIL", Toast.LENGTH_SHORT).show();
                else if(!mInteractionToggleButton.isChecked() && mSuggestionEditText.getText().toString().trim().length() < 8)
                    Toast.makeText(ScrollingFormActivity.this, "Suggestion must be at least 8 characters", Toast.LENGTH_SHORT).show();
                else if(mContextEditText.getText().toString().length() < 8)
                    Toast.makeText(ScrollingFormActivity.this, "Context must be at least 8 characters", Toast.LENGTH_SHORT).show();
                else{
//                    mSendButton.setClickable(true);       TODO Make the button NOT CLICKABLE until all fields are filled.
                    String desc = mDescriptionEditText.getText().toString();;
                    String suggestion;
                    String context = mContextEditText.getText().toString().trim();
                    String TO_EMAIL = mEmailEditText.getText().toString().toUpperCase();
                    int type = mInteractionToggleButton.isChecked()?1:0;
                    if(type == 0){
                        suggestion = mSuggestionEditText.getText().toString();
                        desc = mDescriptionEditText.getText().toString() + "\nSuggestion: " + suggestion;
                    }
                    String event;
                    if(mEventEditText.getVisibility()==View.VISIBLE)
                        event = "\"" + mEventEditText.getText().toString() + "\"";
                    else
                        event = "\"" + mEventSpinner.getSelectedItem().toString() + "\"";
                    sendReport(TO_EMAIL, type, event, desc, context, mAnonymousSwitchCompat.isChecked());
                }
            }
        });


        //Adapter for Spinner
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter.createFromResource(this, R.array.event_types, android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(R.layout.spinner_item);
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
    }

    protected void sendReport(String toEmail, int iatype, String event, String description, String context, boolean isAnonymous){
//        String message = description + "\n Suggestion: " + suggestion;
        Interaction report = new Interaction();

        //Create a report
        report.setToUser(toEmail);
        report.setAnonymous(isAnonymous);
        report.setDescription(description);
        report.setIAType(iatype);
        report.setEventName(event);
        report.setContext(context);
        report.setEventCalendar(Calendar.getInstance());    //TODO Take time from TIME PICKER AND DATE PICKER fragment. PRIORITY: VERY HIGH

        if(report.validateReport(report, getApplicationContext()))
            Log.i("Report Validation: ", "Report successfully validated");
        else
            Log.i("Report Validation: ", "Report NOT validated");

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        if(databaseHelper.addTnterAction(report)==-1)
            Toast.makeText(this, "Could not send InterAction", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(this, "InterAction Sent!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
            startActivity(intent);
            finish();
        }
//        showDialog();
    }
}
