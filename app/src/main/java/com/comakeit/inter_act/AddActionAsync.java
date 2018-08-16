package com.comakeit.inter_act;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AddActionAsync extends AsyncTask<Bundle, Void, Integer> {
    private final String TAG = "ADD_ACTION_ASYNC";

    //Interface to be implemented by calling class to handle response
    public interface ActionSendInterface{
        void processFinish(int response);
    }

    private ActionSendInterface delegate = null;

    public AddActionAsync(ActionSendInterface delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Integer result){
        delegate.processFinish(result);
    }

    @Override
    protected Integer doInBackground(Bundle...bundles){
        Bundle bundle = bundles[0];
        String MAIN_URL = bundle.getString("MAIN_URL") + "/actions";
//        Interaction interaction = bundle.getParcelable("interaction");
        Action action = bundle.getParcelable("action");

        HttpURLConnection httpURLConnection;
        Integer code = -1;
        try{
            URL url = new URL(MAIN_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            Log.e(TAG, "URL -\n  " + MAIN_URL);

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.connect();

            assert action != null;
            JSONObject actionJSON = Utilities.createJsonReport(action);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            if(actionJSON != null) {
                Log.i(TAG, "JSON " + actionJSON.toString());
                outputStream.write(actionJSON.toString().getBytes());
            }
            else return code;

            StringBuilder response = new StringBuilder();
            InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = bufferedReader.readLine())!= null){
                response.append(line);
            }

            outputStream.close();
            code = httpURLConnection.getResponseCode();
            httpURLConnection.disconnect();
            Log.i(TAG, "RESPONSE CODE - " + code + "\n RESPONSE - " + response.toString());
        } catch (IOException ioException) {
            Log.i(TAG,"IO Exception" + ioException.toString());
        }
        return code;

    }

}
