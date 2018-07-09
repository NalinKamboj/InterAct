package com.comakeit.inter_act.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.comakeit.inter_act.GeneralUser;
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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private final AppCompatActivity mActivity = LoginActivity.this;
    private static final int REQUEST_SIGNUP = 0;

    static final String LOGIN_URL = "http://10.0.2.2:8080/inter-act";
    private ScrollView mScrollView;
    private EditText mEmailEditText, mPasswordEditText;
    private TextInputLayout mEmailInputLayout, mPasswordInputLayout;
    private Button mLoginButton;
    private TextView mSignUpTextView;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                VerifyLogin verifyLogin = new VerifyLogin();
                verifyLogin.execute();

                break;
            case R.id.login_signup_text_view:
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);  //startActivityForResult(intent, REQUEST_SIGNUP);
                break;

        }
    }

    private void verifyFromSQLite(){
        if(mDatabaseHelper.checkUser(mEmailEditText.getText().toString().trim().toUpperCase(), mPasswordEditText.getText().toString().trim())){
            UserDetails.setUserEmail(mEmailEditText.getText().toString().trim().toUpperCase());
            Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
            startActivity(intent);
            finish();
        } else
            Snackbar.make(mScrollView, getString(R.string.all_invalid_credentials), Snackbar.LENGTH_LONG).show();
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

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordEditText.setError("Password must be between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordEditText.setError(null);
        }

        return valid;
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        mLoginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.LoginActivityStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private void initViews(){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
        startActivity(intent);
    }

    public void onLoginFailed() {
//        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
//        mLoginButton.setEnabled(true);
    }
    /*
    private void populateAutoComplete() {
        final List<String> names = new ArrayList<>();
        //TODO Listen to the warning and make this class static
        class RetrieveEmployees extends AsyncTask<String, String, String> {
//            private String[] employees;
            protected String doInBackground(String...values){
                StringBuilder result = new StringBuilder();
                HttpURLConnection httpURLConnection = null;
                try{
                    String mainURL = EMPLOYEES_URL + UserDetails.ACCESS_TOKEN;
                    URL authURL = new URL(mainURL);
                    Log.i("EMPLOYEES",mainURL);
                    httpURLConnection = (HttpURLConnection) authURL.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("Accept", "application/json");
                    httpURLConnection.setRequestProperty("Connection", "keep-alive");

                    InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while((line = bufferedReader.readLine())!= null){
                        result.append(line);
                    }
                    Log.i("EMPLOYEES",result.toString());
                } catch(java.io.IOException IOException){
                    Log.i("Retrieving Emp: ","IO Exception while retrieving emp");
                } finally {
                    assert httpURLConnection != null;
                    httpURLConnection.disconnect();
                }
                JSONObject object = null;
                try{
                    JSONArray jsonArray = new JSONArray(result.toString());
                    object = jsonArray.getJSONObject(0);
                    for(int i=1; object!=null; i++){
                        UserDetails.employeesMap.put(object.getString("name"),object.getInt("id"));
                        Log.i("1 OBJECT", object.toString());
                        names.add(object.getString("name"));
                        object = jsonArray.getJSONObject(i);
                    }
                    Log.i("GETTING EMPS: ", jsonArray.toString());
                } catch (org.json.JSONException e){
                    Log.e("Getting Employees", "Malformed JSON " + object.toString());
                }
                return result.toString();
            }
        }

        RetrieveEmployees retrieveEmployees = new RetrieveEmployees();
        retrieveEmployees.execute();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, names);
        mAutoCompleteTextView.setAdapter(adapter);
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setTextColor(Color.BLUE);
    }
    */

    private class VerifyLogin extends AsyncTask<Boolean, Boolean, Boolean> {
        private Boolean verify;
        protected Boolean doInBackground(Boolean...values){
            StringBuilder response = new StringBuilder();
            HttpURLConnection httpURLConnection;
            JSONObject jsonObject = null;
            GeneralUser user = new GeneralUser();
            verify = false;

            try{
                String MAIN_URL = LOGIN_URL + "/user/" + mEmailEditText.getText().toString().trim().toUpperCase();

                URL url = new URL(MAIN_URL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                Log.e("GETTING USER", "POINT 1 REACHED + \n " + MAIN_URL);

                httpURLConnection.setDoInput(true);

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine())!= null){
                    response.append(line);
                }
                Log.i("VERIFY LOGIN RESPONSE", response.toString());
                httpURLConnection.disconnect();

            } catch (IOException ioException) {
                Log.i("Retrieving User Details","IO Exception" + ioException.toString());
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
                    && mEmailEditText.getText().toString().toUpperCase().matches(user.getEmail())){
                onLoginSuccess();
                UserDetails.setUserEmail(user.getEmail());
                UserDetails.setUserID(user.getID());
                UserDetails.setUserName(user.getFirstName() + " " + user.getLastName());
            }
            onLoginFailed();
            return verify;
        }
    }
}

