package com.comakeit.inter_act.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comakeit.inter_act.R;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton;
    private TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailEditText = findViewById(R.id.login_email_edit_text);
        mPasswordEditText = findViewById(R.id.login_password_edit_text);
        mLoginButton = findViewById(R.id.login_button);
        mSignUpTextView = findViewById(R.id.login_signup_text_view);


        mLoginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        mSignUpTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);  //startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
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
        mLoginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        mLoginButton.setEnabled(true);
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
}

