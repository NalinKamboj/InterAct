package com.comakeit.inter_act.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.comakeit.inter_act.Interaction;
import com.comakeit.inter_act.R;
import com.comakeit.inter_act.ReceivedInteractionAdapter;
import com.comakeit.inter_act.sql.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class ReceivedInteractionActivity extends AppCompatActivity {
    private List<Interaction> mInteractionList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ReceivedInteractionAdapter mAdapter;
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_interaction);
        initNavigationDrawer();

//        FloatingActionButton fab = findViewById(R.id.received_interaction_fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
//                startActivity(intent);
//            }
//        });

        mRecyclerView = findViewById(R.id.received_interaction_recycler_view);
        prepareReceivedInteraction();
        mAdapter = new ReceivedInteractionAdapter(getApplicationContext(),mInteractionList);
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

}
