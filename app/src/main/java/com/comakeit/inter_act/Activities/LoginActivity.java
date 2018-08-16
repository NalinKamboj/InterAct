package com.comakeit.inter_act.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.comakeit.inter_act.GeneralUser;
import com.comakeit.inter_act.PreferenceHelper;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//TODO change background to gradient anim background PRIORITY - LOW


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
//    private final AppCompatActivity mActivity = LoginActivity.this;
//    private static final int REQUEST_SIGNUP = 0;

//    static final String LOGIN_URL = "http://10.0.2.2:8080/interact-app";
    private ScrollView mScrollView;
    private EditText mEmailEditText, mPasswordEditText;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    private Button mLoginButton;
    private TextView mSignUpTextView;
    private DatabaseHelper mDatabaseHelper;
    private LinearLayout mProgressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!PreferenceHelper.getInstance().getPreference(this, PreferenceHelper.PREFERENCE_ONBOARDING, false)) {
            Intent onBoarding = new Intent(this, OnboardingActivity.class);
            startActivity(onBoarding);
            finish();
            return;
        }



        initViews();
        initListeners();
        mDatabaseHelper = new DatabaseHelper(this);
        /*
        EMPLOYEES_URL = getResources().getString(R.string.url_employees);
        AUTH_URL = getResources().getString(R.string.authentication_url);
        AUTH_KEY = getResources().getString(R.string.authentication_key_basic);

        //Retrieve unique token from server on creation
        RetrieveTokenTask retrieveTokenTask = new RetrieveTokenTask(AUTH_KEY, AUTH_URL);
        retrieveTokenTask.execute();

         Set up the login form.
        mAutoCompleteTextView = findViewById(R.id.login_username_textview);
        mAutoCompleteTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                populateAutoComplete();
            }
        });
        mLoginButton = findViewById(R.id.email_sign_in_button);

        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAutoCompleteTextView.getText().toString().trim().equals(""))
                    Toast.makeText(getApplicationContext(), "Please enter a valid Username", Toast.LENGTH_SHORT).show();
                else {
                    if(!UserDetails.employeesMap.isEmpty()){
                        UserDetails.userID = UserDetails.employeesMap.get(mAutoCompleteTextView.getText().toString().trim());
                        UserDetails.setUserName(mAutoCompleteTextView.getText().toString().trim());
                        Intent intent = new Intent(getApplicationContext(), TempFormActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(getApplicationContext(), "Could not find users", Toast.LENGTH_SHORT).show();
                }
            }
        });
        */
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.login_button:
//                verifyFromSQLite();
                if (validate()) {
                    VerifyLogin verifyLogin = new VerifyLogin();
                    verifyLogin.execute();
                }
                break;
            case R.id.login_signup_text_view:
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);  //startActivityForResult(intent, REQUEST_SIGNUP);
                break;

        }
    }

    public boolean validate() {
        boolean valid = true;
        String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setError("Enter a valid email address");
            valid = false;
        } else {
            mEmailEditText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4) {
            mPasswordEditText.setError("Password must be greater than 4 characters");
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }

        return valid;
    }

    private void initViews(){
        mProgressBarLayout = findViewById(R.id.progress_bar_linear_layout);
        mProgressBarLayout.setVisibility(View.INVISIBLE);
        mScrollView = findViewById(R.id.activity_login_scroll_view);
        mEmailEditText = findViewById(R.id.login_email_edit_text);
        mPasswordEditText = findViewById(R.id.login_password_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mSignUpTextView = findViewById(R.id.login_signup_text_view);
        mEmailInputLayout = findViewById(R.id.login_email_input_layout);
        mPasswordInputLayout = findViewById(R.id.login_password_input_layout);
    }

    private void initListeners(){
        mLoginButton.setOnClickListener(this);
        mSignUpTextView.setOnClickListener(this);
    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }*/

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Toast.makeText(getApplicationContext(), "Welcome back " + UserDetails.getUserName() + "!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//        mLoginButton.setEnabled(true);
    }

    private class VerifyLogin extends AsyncTask<Void, Integer, Boolean> {
        private Boolean verify;
        private LinearLayout mProgressBarLayout;
        private ProgressBar mProgressBar;

        @Override
        protected void onPreExecute() {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(10);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mProgressBarLayout.setVisibility(View.INVISIBLE);
            if(result)
                onLoginSuccess();
            else
                onLoginFailed();
        }

        VerifyLogin(){
            mProgressBar = findViewById(R.id.progress_bar);
            mProgressBarLayout = findViewById(R.id.progress_bar_linear_layout);
            mProgressBarLayout.setVisibility(View.INVISIBLE);
            mProgressBar.setIndeterminate(false);
            mProgressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.colorLightRed), PorterDuff.Mode.MULTIPLY);
        }

        protected Boolean doInBackground(Void...values){
            StringBuilder response = new StringBuilder();
            HttpURLConnection httpURLConnection;
            JSONObject jsonObject = null;
            GeneralUser user = new GeneralUser();
            verify = false;
            InputStream inputStream;

            try{
                String MAIN_URL = getString(R.string.app_base_url) + "/users/email/" + mEmailEditText.getText().toString().trim().toUpperCase();

                mProgressBar.setProgress(30);
                mProgressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.colorLightRed), PorterDuff.Mode.MULTIPLY);
                URL url = new URL(MAIN_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                Log.e("GETTING USER", "POINT 1 REACHED + \n " + MAIN_URL);

                httpURLConnection.setDoInput(true);

                inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine())!= null){
                    response.append(line);
                }
                Log.i("VERIFY LOGIN RESPONSE", response.toString());
                inputStream.close();
                httpURLConnection.disconnect();
                mProgressBar.setProgress(60);
                mProgressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.colorRoundedAmber), PorterDuff.Mode.MULTIPLY);

            } catch (IOException ioException) {
                Log.i("Retrieving User Details","IO Exception - " + ioException.toString());
            }

            try{
//                JSONArray jsonArray = new JSONArray(response.toString());
                jsonObject = new JSONObject(response.toString());
                Log.e("LOGIN CURRENT JSON", jsonObject.toString());

                user.setEmail(jsonObject.getString("email").toUpperCase());
                user.setPassword(jsonObject.getString("password"));
                user.setID(jsonObject.getInt("id"));
                user.setFirstName(jsonObject.getString("firstName").toUpperCase());
                user.setLastName(jsonObject.getString("lastName").toUpperCase());
            } catch (JSONException exception) {
                Log.e("Getting User", "Malformed JSON ");
            }
            if(mPasswordEditText.getText().toString().matches(user.getPassword())
                    && mEmailEditText.getText().toString().trim().toUpperCase().matches(user.getEmail())){
                verify = true;
                UserDetails.setUserEmail(user.getEmail());
                UserDetails.setUserID(user.getID());
                UserDetails.setUserName(user.getFirstName() + " " + user.getLastName());
                mProgressBar.setProgress(20);
                mProgressBar.getIndeterminateDrawable().setColorFilter(getColor(R.color.colorGreen), PorterDuff.Mode.MULTIPLY);
            }
            return verify;
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}

