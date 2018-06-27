package com.comakeit.inter_act.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_interaction);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.received_interaction_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ScrollingFormActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.received_interaction_recycler_view);
        prepareReceivedInteraction();
        mAdapter = new ReceivedInteractionAdapter(mInteractionList);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void prepareReceivedInteraction() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        mInteractionList = databaseHelper.getReceivedInteraction();
//        Log.i("RECV INTERACTION", "FROM " + mInteractionList.get(2).getFromUserEmail() + " ,DESC " + mInteractionList.get(2).getDescription());
    }

}
