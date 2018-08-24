package com.comakeit.inter_act;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//This Async Task returns a Bundle containing the HTTP response code and String of the JSON object containing all actions of a particular Interaction.
//The doInBackground method takes Bundle as argument which supplies the URL for retrieving actions and the ID of the interaction.
public class GetActionAsync extends AsyncTask<Bundle, Integer, Bundle> {
    private final static String TAG = "GET ACTION ASYNC";

    //Interface to be implemented by calling class for processing the HTTP response
    public interface GetActionInterface{
        void processFinish(Bundle response);
    }

    private GetActionInterface delegate = null;

    //Constructor for passing the required interface
    public GetActionAsync(GetActionInterface getActionInterface){
        this.delegate = getActionInterface;
    }

    @Override
    protected void onPostExecute(Bundle result){
        delegate.processFinish(result);
    }

    @Override
    protected Bundle doInBackground(Bundle...bundles){
        Bundle bundle = bundles[0];
        int interaction_id = bundle.getInt("id");
        String MAIN_URL = bundle.getString("MAIN_URL") + "/reports/" + interaction_id + "/actionList";

        HttpURLConnection httpURLConnection;
        StringBuilder response = new StringBuilder();
        Integer code = -1;
        try{
            URL url = new URL(MAIN_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            Log.e(TAG, "URL -\n  " + MAIN_URL);

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.connect();

            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine())!= null){
                response.append(line);
            }

            code = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            Log.i(TAG, "RESPONSE CODE - " + code + "\n RESPONSE - " + response.toString());
        } catch (IOException ioException) {
            Log.i(TAG,"IO Exception" + ioException.toString());
            code = -1;
        }
        Bundle returnBundle = new Bundle();
        returnBundle.putInt("response", code);  //Adding HTTP Response code to bundle (for returning)
        returnBundle.putString("action_string", response.toString());   //Adding HTTP response as String to bundle for parsing later

        return returnBundle;
    }
}
