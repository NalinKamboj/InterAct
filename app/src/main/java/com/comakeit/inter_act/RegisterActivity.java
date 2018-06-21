package com.comakeit.inter_act;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    private Button mSignUpButton;
    private TextView mLoginLinkTextView;
    private EditText mEmailEditText, mFirstNameEditText, mLastNameEditText, mPasswordEditText, mConfirmPasswordEditText;
    private Drawable mCorrectDrawable;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mSignUpButton = findViewById(R.id.register_signup_button);
        mPasswordEditText = findViewById(R.id.register_password_edit_text);
        mConfirmPasswordEditText = findViewById(R.id.register_confirm_password_edit_text);
        mFirstNameEditText = findViewById(R.id.register_first_name_edit_text);
        mLastNameEditText = findViewById(R.id.register_last_name_edit_text);
        mEmailEditText = findViewById(R.id.register_email_edit_text);
        mLoginLinkTextView = findViewById(R.id.register_login_text_view);
        mCorrectDrawable = getResources().getDrawable(R.drawable.ic_correct, null);
        mCorrectDrawable.setTint(getResources().getColor(R.color.colorGreen, null));

        mEmailEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mFirstNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mLastNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        mConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

        mEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                validateEmail();
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
                validateFirstName();
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
                validateLastName();
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
                validatePassword();
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
                validateConfirmPassword();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mLoginLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mSignUpButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                R.style.LoginActivityStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String first_name = mFirstNameEditText.getText().toString();
        String first_last = mLastNameEditText.getText().toString();
        String email = mEmailEditText.getText().toString();
        String password = mPasswordEditText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess();
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess() {
        mSignUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mSignUpButton.setEnabled(true);
    }

    public boolean validateFirstName(){
        boolean valid = true;
        String first_name = mFirstNameEditText.getText().toString().trim();
        if (first_name.isEmpty() || first_name.length() < 3) {
            mFirstNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mFirstNameEditText.setError("Enter at least 3 characters");
            valid = false;
        } else {
            mFirstNameEditText.setError(null);
            mFirstNameEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
        }
        return valid;
    }

    public boolean validateLastName(){
        boolean valid = true;
        String last_name = mLastNameEditText.getText().toString().trim();
        if (last_name.isEmpty() || last_name.length() < 3) {
            mLastNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mLastNameEditText.setError("Enter at least 3 characters");
            valid = false;
        } else {
            mLastNameEditText.setError(null);
            mLastNameEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
        }

        return valid;
    }

    public boolean validateEmail(){
        boolean valid = true;
        String email = mEmailEditText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mEmailEditText.setError("Enter a valid email address");
            valid = false;
        } else {
            mEmailEditText.setError(null);
            mEmailEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
        }

        return valid;
    }
    public boolean validatePassword(){
        boolean valid = true;
        String password = mPasswordEditText.getText().toString().trim();
        if (password.isEmpty() || password.length() < 4 || password.length() > 18) {
            mPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            mPasswordEditText.setError("Passowrd should be between 4 and 18 characters");
            valid = false;
        } else {
            mPasswordEditText.setError(null);
            mPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
        }
        return valid;
    }
    public boolean validateConfirmPassword(){
        boolean valid = true;
        String confirm_password = mConfirmPasswordEditText.getText().toString().trim();
        if(validatePassword() && confirm_password.equals(mPasswordEditText.getText().toString().trim())) {
            mConfirmPasswordEditText.setError(null);
            mConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(null, null, mCorrectDrawable, null);
        } else
            mConfirmPasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        return valid;
    }

    public boolean validate() {
        boolean valid = true;





        return valid;
    }
}