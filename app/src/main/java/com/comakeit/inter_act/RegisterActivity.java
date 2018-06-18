package com.comakeit.inter_act;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private EditText mNameEditText, mPasswordEditText, mEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mNameEditText = findViewById(R.id.registerNameEditText);
        mEmailEditText = findViewById(R.id.registerEmailEditText);
        mPasswordEditText = findViewById(R.id.registerPasswordEditText);

        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.setCancelable(false);
    }


}
