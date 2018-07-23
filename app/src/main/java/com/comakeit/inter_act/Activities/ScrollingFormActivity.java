package com.comakeit.inter_act.Activities;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.comakeit.inter_act.AsyncResult;
import com.comakeit.inter_act.DateTimePickerFragment;
import com.comakeit.inter_act.GeneralUser;
import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.Utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ScrollingFormActivity extends AppCompatActivity implements DateTimePickerFragment.OnDataPass{
    private Spinner mEventSpinner;
    private EditText mEventEditText, mDescriptionEditText, mSuggestionEditText, mEmailEditText, mContextEditText;
    private TextSwitcher mAnonymousTextSwitcher;
    private ToggleButton mInteractionToggleButton;
    private FloatingActionButton mFloatingActionButton;
    private SwitchCompat mAnonymousSwitchCompat;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private Menu appBarMenu;
    private AutoCompleteTextView mAutoCompleteTextView;
//    private Window window;
    private Calendar eventCalendar;
    private Interaction report;
    private final String INTERACTION_URL = "http://10.0.2.2:8080/interact-app/reports";
    private final String APP_URL = "http://10.0.2.2:8080/interact-app";
    final List<String> userEmails = new ArrayList<>();
    ArrayList<GeneralUser> generalUsers = new ArrayList<>();

    //For animation
    private LottieAnimationView mLottieAnimationView;
    private LinearLayout mAnonymousLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setExitTransition(new Explode());       //Sets exit transition for the "CALLING" activity!
        setContentView(R.layout.activity_scrolling_form);

        /* NOT CHANGING STATUS BAR COLOR HERE EW

        //Setting up flags to be able to change status bar color
        window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRoundedAmberDark));      //Changing status bar color
        */

        initNavigationDrawer();
        initViews();
        populateAutoComplete();
    }

    private void initNavigationDrawer(){
        mNavigationView = findViewById(R.id.scrolling_navigation_view);
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        Toolbar toolbar = findViewById(R.id.new_interaction_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

//        String parts[] = UserDetails.getUserName().split(" ");
        String welcome = "Welcome " + Utilities.toCamelCase(UserDetails.getUserName());
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
                                Intent receivedInteractionIntent = new Intent(getApplicationContext(), ReceivedInteractionActivity.class);
                                startActivity(receivedInteractionIntent);
                                break;
                            case R.id.menu_sent_interaction:
                                Intent sentInteractionIntent = new Intent(getApplicationContext(), SentInteractionActivity.class);
                                startActivity(sentInteractionIntent);
                                break;
                            case R.id.menu_settings:
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
        eventCalendar = null;
        //Initialize all widgets and elements
        mContextEditText = findViewById(R.id.new_interaction_context_edit_text);
//        mAnonymousSwitchCompat = findViewById(R.id.new_interaction_anonymous_switch);
        mAnonymousTextSwitcher = findViewById(R.id.new_interaction_anonymous_text_switcher);
        mEventSpinner = findViewById(R.id.new_interaction_event_spinner);
        mEventEditText = findViewById(R.id.new_interaction_event_edit_text);
        mAutoCompleteTextView = findViewById(R.id.new_interaction_email_text_view);
        mFloatingActionButton = findViewById(R.id.new_interaction_fab);
//        mEmailEditText = findViewById(R.id.new_interaction_email_edit_text);
        mDescriptionEditText = findViewById(R.id.new_interaction_description_edit_text);
        mInteractionToggleButton = findViewById(R.id.new_interaction_interaction_toggle_button);
        mDescriptionEditText = findViewById(R.id.new_interaction_description_edit_text);
        mSuggestionEditText = findViewById(R.id.new_interaction_suggestion_edit_text);
        mSuggestionEditText.setVisibility(View.VISIBLE);

        //Animation stuff
        mAnonymousLinearLayout = findViewById(R.id.new_interaction_anonymous_layout);
        mLottieAnimationView = findViewById(R.id.new_interaction_anonymous_lottie);

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
                    mTransition.startTransition(250);       //Start transition here because button is initially NOT CHECKED.
                    mSuggestionEditText.setVisibility(View.GONE);
                } else{
                    mTransition.reverseTransition(250);
                    mSuggestionEditText.setVisibility(View.VISIBLE);
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
//        mAnonymousSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(mAnonymousSwitchCompat.isChecked())
//                    mAnonymousTextSwitcher.setText(getResources().getString(R.string.all_anonymous));
//                else
//                    mAnonymousTextSwitcher.setText(getResources().getString(R.string.all_not_anonymous));
//            }
//        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Check data entered in all fields and send report
                if(mDescriptionEditText.getText().toString().trim().length() < 8)
                    Toast.makeText(ScrollingFormActivity.this, "Feedback/Appreciation can't be left blank or less than 8 characters"
                            , Toast.LENGTH_SHORT).show();
                else if(!Patterns.EMAIL_ADDRESS.matcher(mAutoCompleteTextView.getText().toString()).matches())
                    Toast.makeText(ScrollingFormActivity.this, "Please enter a valid EMAIL", Toast.LENGTH_SHORT).show();
                else if(!mInteractionToggleButton.isChecked() && mSuggestionEditText.getText().toString().trim().length() < 8)
                    Toast.makeText(ScrollingFormActivity.this, "Suggestion must be at least 8 characters", Toast.LENGTH_SHORT).show();
                else if(mContextEditText.getText().toString().length() < 8)
                    Toast.makeText(ScrollingFormActivity.this, "Context must be at least 8 characters", Toast.LENGTH_SHORT).show();
                else if(eventCalendar == null)
                    Toast.makeText(ScrollingFormActivity.this, "Please enter date and time", Toast.LENGTH_SHORT).show();
                else{
                    int id = -1;
//                    mSendButton.setClickable(true);       TODO Make the button NOT CLICKABLE until all fields are filled.
                    for(GeneralUser user: generalUsers) {
                        Log.i("USERS IN SCROLL", "USER - " + user.getID() + " email - " + user.getEmail());
                        if(user.getEmail().toUpperCase().matches(mAutoCompleteTextView.getText().toString().trim().toUpperCase()))
                           id = user.getID();
                    }
                    if (id == -1) {
                        Snackbar.make(mDrawerLayout, "Invalid Recipient", Snackbar.LENGTH_SHORT).show();
                        return;
                    }
                    String desc = mDescriptionEditText.getText().toString();;
                    String suggestion = "";
                    String context = mContextEditText.getText().toString().trim();
                    String TO_EMAIL = mAutoCompleteTextView.getText().toString().toUpperCase();
                    int type = mInteractionToggleButton.isChecked()?1:0;
                    if(type == 0){
                        suggestion = mSuggestionEditText.getText().toString();
                    }
                    String event;
                    if(mEventEditText.getVisibility()==View.VISIBLE)
                        event = mEventEditText.getText().toString();
                    else
                        event = mEventSpinner.getSelectedItem().toString();
                    boolean anonmyous = !(mLottieAnimationView.getProgress() == 0);
                    sendReport(TO_EMAIL, type, event, desc, suggestion, context, anonmyous, id);
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

        //Listener for anonymous switch
        mAnonymousLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCheckAnimation();

            }
        });
    }

    /**
     * Function for anonymous toggle lottie animation
     */
    private void startCheckAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 0.46f).setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mLottieAnimationView.setProgress((Float) valueAnimator.getAnimatedValue());
            }
        });

        if(mLottieAnimationView.getProgress() == 0f)
            valueAnimator.start();
        else
            valueAnimator.reverse();
//            mLottieAnimationView.setProgress(0f);
    }

    protected void sendReport(String toEmail, int iatype, String event, String observation, String recommendation, String context, boolean isAnonymous, int id){
//        String message = description + "\n Suggestion: " + suggestion;
        report = new Interaction();
        //Validate email id
        if (toEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(toEmail).matches()){
            Snackbar.make(mDrawerLayout, "Please enter valid email", Snackbar.LENGTH_SHORT).show();
            return;
        }

        //Create a report
        report.setToUserEmail(toEmail);
        report.setToUserId((long)id);
        report.setFromUserId(UserDetails.getUserID());
        report.setAnonymous(isAnonymous);
        report.setObservation(observation);
        report.setType(iatype);
        report.setRecommendation(recommendation);
        report.setEventName(event);
        report.setContext(context);
        Log.i("Context and ID", context + " ID - " + id);

        Date eventDate = eventCalendar.getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");   //TODO Add LOCALE. PRIORITY: LOW

        report.setEventDateDate(eventDate);

        Log.e("FORM EVENT DATE", "DATE - " + eventDate.toString());
        if(report.validateReport(report, getApplicationContext()))
            Log.i("Report Validation: ", "Report successfully validated");
        else
            Log.i("Report Validation: ", "Report NOT validated");




        //The code below this is working --

        PublishInterAction publishInterAction = new PublishInterAction();
        publishInterAction.execute();
//        finish();
    }

    @Override
    public void onDataPass(Calendar data){
        if(data != null) {
            eventCalendar = data;
        }
        else
            Toast.makeText(getApplicationContext(), "Invalid ARG in onDataPass()", Toast.LENGTH_SHORT).show();
    }

    private class PublishInterAction extends AsyncTask<Boolean, Boolean, Integer> {
        public AsyncResult taskResult = null;
        private Boolean sent;

        @Override
        protected void onPostExecute(Integer result){
            switch (result) {
                case 200:
                    Toast.makeText(getApplicationContext(), "InterAction sent!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),ScrollingFormActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    Toast.makeText(getApplicationContext(), "Sending failed (unknown error)", Toast.LENGTH_SHORT).show();
            }
        }

        protected Integer doInBackground(Boolean...values){
            HttpURLConnection httpURLConnection;
            Integer code = -1;
            try{
                String MAIN_URL = getString(R.string.app_base_url) + "/interactions";
                URL url = new URL(MAIN_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                Log.e("POSTING IA", "POINT 1 REACHED + \n " + MAIN_URL);

                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.connect();

                JSONObject reportJSON = Utilities.createJsonReport(report);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                if(reportJSON != null)
                    outputStream.write(reportJSON.toString().getBytes());
                else return code;

                outputStream.close();
                code = httpURLConnection.getResponseCode();
                httpURLConnection.disconnect();
//                String message = httpURLConnection.get;
                Log.i("PUBLISH INTERACTION", "RESPONSE CODE - " + code);
                sent = true;
            } catch (IOException ioException) {
                Log.i("PUBLISH INTERACTION","IO Exception" + ioException.toString());
            }
            return code;
        }
    }


    //Method which is run after PublishInteraction finishes...
//    @Override
//    public void processFinish(boolean result) {
//        if(result) {
//            Intent intent = new Intent(this, ScrollingFormActivity.class);
//            startActivity(intent);
//            finish();
//        } else
//            Snackbar.make(mDrawerLayout, "Could not sent InterAction", Snackbar.LENGTH_SHORT).show();
//    }

    /**
     * Function for getting details of all users to whom an InterAction can be sent, and adding those users into autocomplete of the text view
     */
    private void populateAutoComplete() {
        //TODO Listen to the warning and make this class static
        class RetrieveUsers extends AsyncTask<String, String, String> {
            protected String doInBackground(String...values){
                StringBuilder result = new StringBuilder();
                HttpURLConnection httpURLConnection = null;

                try{
                    URL usersURL = new URL(getString(R.string.app_base_url) + "/users");
                    Log.i("EMPLOYEES", usersURL.toString());
                    httpURLConnection = (HttpURLConnection) usersURL.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoInput(true);
//                    httpURLConnection.setRequestProperty("Accept", "application/json");
//                    httpURLConnection.setRequestProperty("Connection", "keep-alive");

                    InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = bufferedReader.readLine())!= null){
                        result.append(line);
                    }
//                    Log.i("EMPLOYEES",result.toString());
                } catch(java.io.IOException IOException){
                    Log.i("Retrieving Emp: ","IO Exception while retrieving emp");
                } finally {
                    assert httpURLConnection != null;
                    httpURLConnection.disconnect();
                }
                Log.e("RESPONSE USERS", result.toString());
                try{
//                    JSONObject outerObject = new JSONObject(result.toString());
//                    Log.e("OUTER OBJECT", outerObject.toString());
//                    JSONObject innerObject = outerObject.getJSONObject("_embedded");
//                    JSONArray mainArray = innerObject.getJSONArray("users");
//                    Log.i("JSON ARRAY", result.toString());
//                    JSONObject mainObject = new JSONObject(result.toString());
//                    if(mainObject!=null) {
//                        JSONArray usersList = mainObject.getJSONArray();
//
//                    }
                    JSONArray userJSONArray = new JSONArray(result.toString());

                    Log.i("AC JSON ARRAY", userJSONArray.toString());
//                    object = jsonArray.getJSONObject(0);
                    JSONObject userObject = userJSONArray.getJSONObject(0);
                    if(userObject != null) {
                        Log.e("FIRST USER", userObject.toString());
                        for(int i=1; userObject!=null; i++){
                            if(userObject.getLong("id") != UserDetails.getUserID()) {
                                Log.i("SCROLL AUTO-C OBJECT", userObject.toString());
                                GeneralUser user = new GeneralUser();
                                user.setFirstName(userObject.getString("firstName"));
                                user.setLastName(userObject.getString("lastName"));
                                user.setID(userObject.getInt("id"));
                                user.setEmail(userObject.getString("email"));
                                generalUsers.add(user);
                                GeneralUser.sUserHashMap.put((long)user.getID(), user);
                                userEmails.add(user.getEmail());
//                                userNames.add(object.getString("name"));
                            }
                            userObject = userJSONArray.getJSONObject(i);
                        }
                    }
                } catch (org.json.JSONException e){
                    Log.e("Getting Employees", "Malformed JSON");
                }

                //HASH MAP TEST
                for(Map.Entry map : GeneralUser.sUserHashMap.entrySet()){
                    GeneralUser generalUser = (GeneralUser) map.getValue();
                    Log.i("HASH MAP TEST", " User - " + map.getKey() + " " + ((GeneralUser) map.getValue()).getFirstName() + "\n");
                }

                return result.toString();
            }
        }

        RetrieveUsers retrieveUsers = new RetrieveUsers();
        retrieveUsers.execute();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, userEmails);
        mAutoCompleteTextView.setAdapter(adapter);
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setTextColor(Color.BLUE);
    }
}
