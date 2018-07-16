package com.comakeit.inter_act.Activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.comakeit.inter_act.GeneralUser;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.sql.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private final AppCompatActivity mActivity = RegisterActivity.this;
    private Button mSignUpButton;
    private TextView mLoginLinkTextView;
    private EditText mEmailEditText, mFirstNameEditText, mLastNameEditText, mPasswordEditText, mConfirmPasswordEditText;
    private Drawable mCorrectDrawable;
    private TextInputLayout mEmailInputLayout, mFirstNameInputLayout, mLastNameInputLayout, mPasswordInputLayout, mConfirmPasswordInputLayout;
    private DatabaseHelper mDatabaseHelper;
    private UserDetails mUserDetails;
    private ScrollView mRegisterScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        mDatabaseHelper = new DatabaseHelper(mActivity);
        mUserDetails = new UserDetails();

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(validateEmail()){
                    mEmailInputLayout.setErrorEnabled(false);
                    mEmailEditText.setCompoundDrawables(null, null, mCorrectDrawable, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mFirstNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(validateFirstName()){
                    mFirstNameInputLayout.setErrorEnabled(false);
                    mFirstNameEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mLastNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (validateLastName()){
                    mLastNameInputLayout.setErrorEnabled(false);
                    mLastNameEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(validatePassword()){
                    mPasswordInputLayout.setErrorEnabled(false);
                    mPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mConfirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!validateConfirmPassword()){
                    mConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                    mConfirmPasswordInputLayout.setErrorEnabled(true);
                    mConfirmPasswordInputLayout.setError("Passwords do not match");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail() && validateFirstName() && validateLastName() && validatePassword() && validateConfirmPassword()){

                    GeneralUser generalUser = new GeneralUser();
                    generalUser.setEmail(mEmailEditText.getText().toString().toUpperCase());
                    generalUser.setFirstName(mFirstNameEditText.getText().toString());
                    generalUser.setLastName(mLastNameEditText.getText().toString());
                    generalUser.setPassword(mPasswordEditText.getText().toString());

                    UserDetails.setUserName(mFirstNameEditText.getText().toString().trim().toUpperCase()
                            + " " + mLastNameEditText.getText().toString().trim().toUpperCase());
                    UserDetails.setUserPassword(mPasswordEditText.getText().toString().trim());
                    UserDetails.setUserEmail(mEmailEditText.getText().toString().trim().toUpperCase());

                    RegisterUser registerUser = new RegisterUser();
                    registerUser.execute(generalUser);
//                    mDatabaseHelper.addUser(mUserDetails);
//                    Snackbar.make(mRegisterScrollView, getString(R.string.register_successful), Snackbar.LENGTH_LONG).show();
//                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent);
//                    finish();
                }
            }
        });
        mLoginLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews(){
        mRegisterScrollView = findViewById(R.id.register_scroll_view);
        mSignUpButton = findViewById(R.id.register_signup_button);
        mPasswordEditText = findViewById(R.id.register_password_edit_text);
        mConfirmPasswordEditText = findViewById(R.id.register_confirm_password_edit_text);
        mFirstNameEditText = findViewById(R.id.register_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.register_last_name_edit_text);
        mEmailEditText = findViewById(R.id.register_email_edit_text);
        mLoginLinkTextView = findViewById(R.id.register_login_text_view);
        mCorrectDrawable = getResources().getDrawable(R.drawable.ic_correct, null);
        mCorrectDrawable.setBounds(0,0, mCorrectDrawable.getIntrinsicWidth(), mCorrectDrawable.getIntrinsicHeight());
        mCorrectDrawable.setTint(getResources().getColor(R.color.colorGreen, null));

        mEmailInputLayout = findViewById(R.id.register_email_input_layout);
        mFirstNameInputLayout = findViewById(R.id.register_first_name_input_layout);
        mLastNameInputLayout = findViewById(R.id.register_last_name_input_layout);
        mPasswordInputLayout = findViewById(R.id.register_password_input_layout);
        mConfirmPasswordInputLayout = findViewById(R.id.register_confirm_password_input_layout);
    }

    public void onSignupSuccess() {
        mSignUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();

        mSignUpButton.setEnabled(true);
    }

    public boolean validateFirstName(){
        boolean valid = true;
        String first_name = mFirstNameEditText.getText().toString().trim();
        if (first_name.isEmpty() || first_name.length() < 3) {
            mFirstNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mFirstNameInputLayout.setErrorEnabled(true);
            mFirstNameInputLayout.setError("At least 3 letters");
            valid = false;
        }
        return valid;
    }
    public boolean validateLastName(){
        boolean valid = true;
        String last_name = mLastNameEditText.getText().toString().trim();
        if (last_name.isEmpty() || last_name.length() < 3) {
            mLastNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mLastNameInputLayout.setErrorEnabled(true);
            mLastNameInputLayout.setError("At least 3 letters");
            valid = false;
        }
        return valid;
    }
    public boolean validateEmail(){
        boolean valid = true;
        String email = mEmailEditText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mEmailInputLayout.setErrorEnabled(true);
            mEmailInputLayout.setError("Enter a valid email address");
            valid = false;
        }
        return valid;
    }
    public boolean validatePassword(){
        boolean valid = true;
        String password = mPasswordEditText.getText().toString().trim();
        if (password.isEmpty() || password.length() < 4 || password.length() > 18) {
            mPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mPasswordInputLayout.setErrorEnabled(true);
            mPasswordInputLayout.setError("Must be between 4 and 18 letters");
            valid = false;
        }
        return valid;
    }
    public boolean validateConfirmPassword(){
        boolean valid = false;
        String confirm_password = mConfirmPasswordEditText.getText().toString().trim();
        if(validatePassword() && confirm_password.equals(mPasswordEditText.getText().toString().trim())) {
            valid = true;
            mConfirmPasswordInputLayout.setErrorEnabled(false);
            mConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
        }
        return valid;
    }

    //Async task class for sending user registration data to server
    private class RegisterUser extends AsyncTask<GeneralUser, Integer, Boolean> {

        @Override
        protected void onPostExecute(Boolean result) {
            if (result){
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Couldn't complete registration", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(GeneralUser... generalUsers) {
            Boolean result = false;
            HttpURLConnection httpURLConnection;
            int responseCode = -1;

            GeneralUser generalUser = new GeneralUser();
            generalUser = generalUsers[0];

            try{
                //Establish connection with server
                URL url = new URL(getString(R.string.app_base_url) + "/users/");
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.connect();
                Log.i("REGISTER ACTIVITY", "URL " + url.toString());

                //Create JSON object
                JSONObject userJSON = new JSONObject();
                userJSON.put("firstName", generalUser.getFirstName());
                userJSON.put("lastName", generalUser.getLastName());
                userJSON.put("email", generalUser.getEmail());
                userJSON.put("password", generalUser.getPassword());
                Log.i("REGISTER ACTIVITY", userJSON.toString());

                //Write Data to stream
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(userJSON.toString().getBytes());

                //Get and log response code
                responseCode = httpURLConnection.getResponseCode();
                Log.i("REGISTER ACTIVITY", "RESPONSE " + responseCode);

                //Close connection and stream
                outputStream.close();
                httpURLConnection.disconnect();
                result = true;

            } catch (MalformedURLException e) {
                Log.e("REGISTER ACTIVITY URL", e.toString());
            } catch (IOException e) {
                Log.e("REGISTER ACTIVITY HTTP", e.toString());
            } catch (JSONException e) {
                Log.e("REGISTER ACTIVITY JSON", e.toString());
            }
            return result;
        }
    }
}