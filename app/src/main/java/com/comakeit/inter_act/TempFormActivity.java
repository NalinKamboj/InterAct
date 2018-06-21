package com.comakeit.inter_act;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TempFormActivity extends AppCompatActivity {
    private AutoCompleteTextView mAutoCompleteTextView;
    private EditText mFeedbackEditText, mActionsEditText;   //Both should be multiline
    private String FEEDBACK_URL;
    private Button sendFormButton;
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private int TO_ID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_form);

        FEEDBACK_URL = getResources().getString(R.string.url_feedbacks);
        mAutoCompleteTextView = findViewById(R.id.temp_to_text_view);
        mActionsEditText = findViewById(R.id.temp_actions_edit_text);
        mFeedbackEditText = findViewById(R.id.temp_feedback_edit_text);
        sendFormButton = findViewById(R.id.temp_send_button);


        //Navigation view stuff...
        mToolbar = findViewById(R.id.simple_toolbar);
        mNavigationView = findViewById(R.id.temp_navigation_view);
        mNavigationView.setCheckedItem(R.id.menu_new_interaction);
        setSupportActionBar(mToolbar);
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
                        }

                        return true;
                    }
                }
        );

        mAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateAutoComplete();
            }
        });

        sendFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mFeedbackEditText.getText().toString().trim().equals("") || mAutoCompleteTextView.getText().toString().trim().equals("") ||
                        mActionsEditText.getText().toString().trim().equals(""))
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                else{
                    TO_ID = UserDetails.employeesMap.get(mAutoCompleteTextView.getText().toString());
                    if(TO_ID == -1)
                        Toast.makeText(getApplicationContext(), "Please enter a valid recipient", Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(getApplicationContext(), "Sending feedback...", Toast.LENGTH_SHORT).show();
                        SendDataTask sendDataTask = new SendDataTask();
                        sendDataTask.execute();

                        Intent intent = new Intent(getApplicationContext(), TempFormActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    class SendDataTask extends AsyncTask<String, String, String> {      //TODO Make this static (and the one in LoginActivity) to prevent leaks as suggested
        protected String doInBackground(String...values){
            String postData = "";
            HttpURLConnection httpURLConnection = null;
            try{
                String mainURL = FEEDBACK_URL + UserDetails.ACCESS_TOKEN;
                URL authURL = new URL(mainURL);
                Log.i("Feedback URL ",mainURL);
                httpURLConnection = (HttpURLConnection) authURL.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setRequestProperty("Connection", "keep-alive");
                postData = "{\"id\": \"\", \"feedback\": \"" + mFeedbackEditText.getText().toString().trim() +
                        "\", \"employeeId\": " + UserDetails.employeesMap.get(mAutoCompleteTextView.getText().toString().trim())
                        + ", \"receivedFrom\": \"" + UserDetails.getUserName() + "\", \"actionsTaken\": \""
                        + mActionsEditText.getText().toString().trim() + "\", \"createdAt\": \"\"}";
                OutputStreamWriter writer = new OutputStreamWriter(httpURLConnection.getOutputStream());
                writer.write(postData);
                writer.close();

                BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer jsonString = new StringBuffer();
                String line;
                int statusCode = -1;
                statusCode = httpURLConnection.getResponseCode();

                while((line = br.readLine()) != null){
                    jsonString.append(line);
                }
                br.close();
                Log.i("Feedback Sender", postData);
                Log.i("Feedback Received", jsonString.toString() + "RESPONSE CODE: " + statusCode);
//                if(statusCode != -1){
//                    switch (statusCode){
//                        case 200:
//                            Toast.makeText(getApplicationContext(), "Feedback Successfully Submitted!", Toast.LENGTH_SHORT).show();
//                        case 400:
//                            Toast.makeText(getApplicationContext(), "Feedback not submitted. Bad Request", Toast.LENGTH_SHORT).show();
//                    }
//                }
            } catch(java.io.IOException IOException){
                Log.i("Sending FB","IO Exception while sending.");
            } finally {
                assert httpURLConnection != null;
                httpURLConnection.disconnect();
            }
            return postData;
        }
    }

    private void populateAutoComplete() {
        final List<String> names = new ArrayList<>();
        names.addAll(UserDetails.employeesMap.keySet());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, names);
        mAutoCompleteTextView.setAdapter(adapter);
        mAutoCompleteTextView.setThreshold(1);
        mAutoCompleteTextView.setTextColor(Color.BLUE);
    }
}
