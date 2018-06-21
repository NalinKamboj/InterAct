package com.comakeit.inter_act;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private String EMPLOYEES_URL;
    private String AUTH_URL;
    private String AUTH_KEY;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private AutoCompleteTextView mAutoCompleteTextView;
    private Button mLoginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        /*
        EMPLOYEES_URL = getResources().getString(R.string.url_employees);
        AUTH_URL = getResources().getString(R.string.authentication_url);
        AUTH_KEY = getResources().getString(R.string.authentication_key_basic);

        //Retrieve unique token from server on creation
        RetrieveTokenTask retrieveTokenTask = new RetrieveTokenTask(AUTH_KEY, AUTH_URL);
        retrieveTokenTask.execute();

        // Set up the login form.
//        mAutoCompleteTextView = findViewById(R.id.login_username_textview);
        mAutoCompleteTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                populateAutoComplete();
            }
        });
//        mLoginButton = findViewById(R.id.email_sign_in_button);

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
}

