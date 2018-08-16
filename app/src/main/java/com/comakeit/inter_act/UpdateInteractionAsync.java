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

public class UpdateInteractionAsync extends AsyncTask<Bundle, Void, Integer> {
    //Interface to be implemented by calling class
    public interface UpdateInterface{
        void processFinish(int response);
    }

    private UpdateInterface delegate = null;

    UpdateInteractionAsync(UpdateInterface delegate){
        this.delegate = delegate;
    }

    @Override
    protected void onPostExecute(Integer result){
        delegate.processFinish(result);
    }

    @Override
    protected Integer doInBackground(Bundle...bundles){
        Bundle bundle = bundles[0];
        String MAIN_URL = bundle.getString("MAIN_URL") + "/reports";
        Interaction interaction = bundle.getParcelable("interaction");

        HttpURLConnection httpURLConnection;
        Integer code = -1;
        try{
            assert interaction != null;
            Long id = interaction.getInteractionID();

            String UPDATE_URL = MAIN_URL + "/" + id;
            URL url = new URL(UPDATE_URL);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("PUT");
            Log.e("UPDATE IA", "URL -\n  " + UPDATE_URL);

            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            httpURLConnection.connect();

            JSONObject reportJSON = Utilities.createJsonReport(interaction, id);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            if(reportJSON != null) {
                Log.i("UPDATE INTERACTION", "JSON " + reportJSON.toString());
//                reportJSON.put("id", id);
                outputStream.write(reportJSON.toString().getBytes());
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
//                String message = httpURLConnection.get;
            Log.i("UPDATE INTERACTION", "RESPONSE CODE - " + code + "\n RESPONSE - " + response.toString());
        } catch (IOException ioException) {
            Log.i("UPDATE INTERACTION","IO Exception" + ioException.toString());
        }
        return code;
    }
}
