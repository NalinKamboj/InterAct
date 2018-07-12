package com.comakeit.inter_act.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.ReceivedInteractionAdapter;
import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.sql.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ReceivedInteractionActivity extends AppCompatActivity {
    private List<Interaction> mInteractionList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ReceivedInteractionAdapter mAdapter;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_interaction);
        initNavigationDrawer();

        //Setting up flags to be able to change status bar color
        window = this.getWindow();
        window.setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorTranslucent));      //Changing status bar color

//        FloatingActionButton fab = findViewById(R.id.received_interaction_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
//                startActivity(intent);
//            }
//        });

        /* DEBUG TASK */
        getReports getReports = new getReports();
        getReports.execute();
        mAdapter = new ReceivedInteractionAdapter(getApplicationContext(),mInteractionList);
        mRecyclerView = findViewById(R.id.received_interaction_recycler_view);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initNavigationDrawer(){
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        mNavigationView = findViewById(R.id.received_interaction_navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String parts[] = UserDetails.getUserEmail().split("@");
        String welcome = "Welcome " + parts[0];
        View headerView = mNavigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.navigation_view_header_text_view);
        navUserName.setText(welcome);

        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.menu_received_interaction).setChecked(true);
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        switch (item.getItemId()){
                            case R.id.menu_new_interaction:
                                Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case R.id.menu_logout:
                                Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(logoutIntent);
                                finish();
                                break;
                            case R.id.menu_received_interaction:
//                                DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
//                                databaseHelper.tempFunc(getApplicationContext());
//                                Snackbar.make(mDrawerLayout, getString(R.string.all_under_dev), Snackbar.LENGTH_LONG).show();
//                                Intent receivedInteractionIntent = new Intent(getApplicationContext(), ReceivedInteractionActivity.class);
//                                startActivity(receivedInteractionIntent);
                                break;
                            case R.id.menu_sent_interaction:
                                Snackbar.make(mDrawerLayout, getString(R.string.all_under_dev), Snackbar.LENGTH_LONG).show();
                                break;
                        }

                        return true;
                    }
                }
        );
    }

    private void prepareReceivedInteraction() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        mInteractionList = databaseHelper.getReceivedInteraction();
//        Log.i("RECV INTERACTION", "FROM " + mInteractionList.get(2).getFromUserEmail() + " ,DESC " + mInteractionList.get(2).getDescription());
    }

    private class getReports extends AsyncTask<Void, Integer, Boolean> {


        protected Boolean doInBackground(Void...params) {
            Log.i("I AM RUNNING", "OH JEEZ");
            Boolean result = false;
            URL getInteractionURL = null;
            HttpURLConnection httpURLConnection;
            StringBuilder response = new StringBuilder();

            try{
                getInteractionURL = new URL(getString(R.string.app_base_url) + "/users/" + UserDetails.getUserID());
            } catch (MalformedURLException e) {
                Log.e("RECEIVED IA (ASYNC)", "Malformed URL " + e.toString());
            }
            //Increase progress here...


            //Open HTTP connection and get the data
            try{
                assert getInteractionURL != null;
                httpURLConnection = (HttpURLConnection) getInteractionURL.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                httpURLConnection.connect();

                InputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line = bufferedReader.readLine())!= null){
                    response.append(line);
                }
                Log.i("RECEIVED IA RESPONSE", response.toString());
                httpURLConnection.disconnect();

                //Extract InterActions from the received response
                JSONObject userData = new JSONObject(response.toString());
                JSONArray receivedReportsList = userData.getJSONArray("reportReceivedList");
                JSONObject reportJSON = new JSONObject();
                result = true;
                for(int i = 0; i < receivedReportsList.length() ; i++) {
                    reportJSON = receivedReportsList.getJSONObject(i);
                    Log.i("RECEIVED IA - REPORTS", reportJSON.toString());
                    Interaction interaction = new Interaction();
                    interaction.setToUserId(UserDetails.getUserID());
                    interaction.setToUserEmail(UserDetails.getUserEmail());
                    interaction.setFromUserId(reportJSON.getLong("fromUserId"));
                    interaction.setEventName(reportJSON.getString("eventName"));
                    interaction.setEventDate(reportJSON.getString("eventDate"));
                    interaction.setObservation(reportJSON.getString("observation"));
                    interaction.setContext(reportJSON.getString("context"));
                    interaction.setRecommendation(reportJSON.getString("recommendation"));
                    interaction.setType(reportJSON.getInt("type"));
                    interaction.setAnonymous(reportJSON.getBoolean("anonymous"));
                    mInteractionList.add(interaction);
                    mAdapter.notifyDataSetChanged();
                }

            } catch (IOException e) {
                Log.e("RECEIVED IA ", e.toString());
            } catch (JSONException e) {
                Log.e("RECEIVED IA", e.toString());
            }

            for(int i = 0; i < mInteractionList.size(); i++) {
                Log.i("FROM IA ", mInteractionList.get(i).getObservation().toUpperCase());
            }

            return result;
        }
    }
}
