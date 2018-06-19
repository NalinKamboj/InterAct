package com.comakeit.inter_act;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RetrieveTokenTask extends AsyncTask<String, String, String> {
    private String AUTH_KEY;
    private String AUTH_URL;

    RetrieveTokenTask(String key, String url){
        this.AUTH_KEY = key;
        this.AUTH_URL = url;
    }
    protected String doInBackground(String...values){
        StringBuilder result = new StringBuilder();
        HttpURLConnection httpURLConnection = null;
        try{
            URL authURL = new URL(this.AUTH_URL);
            httpURLConnection = (HttpURLConnection) authURL.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", this.AUTH_KEY);

            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine())!= null){
                result.append(line);
            }
//            Log.i("Aysnc Token: ", result.toString());
        } catch(java.io.IOException IOException){
            Log.i("Login Activity: ","IO Exception while retrieving token");
        } finally {
            assert httpURLConnection != null;
            httpURLConnection.disconnect();
        }
        JSONObject object = null;
        String token = null;
        try{
            object = new JSONObject(result.toString());
            Log.i("GETTING TOKEN: ", object.toString());
            token = object.getString("access_token");
        } catch (org.json.JSONException e){
            Log.e("GETTING TOKEN EXC", "Malformed JSON:");
        }
        Log.i("Retrieved Token: ", token);
        UserDetails.setToken(token);
        return token;
    }
}
