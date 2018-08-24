package com.comakeit.inter_act.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.comakeit.inter_act.R;
import com.comakeit.inter_act.UserDetails;
import com.comakeit.inter_act.Utilities;

import es.dmoral.toasty.Toasty;

public class AboutActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initNavigationDrawer();
    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(mNavigationView))
            mDrawerLayout.closeDrawer(Gravity.START);
    }

    private void initNavigationDrawer(){
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        mNavigationView = findViewById(R.id.about_navigation_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String welcome = "Welcome " + Utilities.toCamelCase(UserDetails.getUserName());
        View headerView = mNavigationView.getHeaderView(0);
        TextView navUserName = headerView.findViewById(R.id.navigation_view_header_text_view);
        navUserName.setText(welcome);

        Menu menu = mNavigationView.getMenu();
        menu.findItem(R.id.menu_about).setChecked(true);
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
                                Intent receivedIntent = new Intent(getApplicationContext(), ReceivedInteractionActivity.class);
                                startActivity(receivedIntent);
                                break;
                            case R.id.menu_sent_interaction:
                                Intent sentInteractionIntent = new Intent(getApplicationContext(), SentInteractionActivity.class);
                                startActivity(sentInteractionIntent);
                                break;
                            case R.id.menu_settings:
                                Toasty.info(getApplicationContext(), getString(R.string.all_under_dev), Toast.LENGTH_LONG, true).show();
                                break;
                            case R.id.menu_my_actions:
                                Toasty.info(getApplicationContext(), getString(R.string.all_under_dev), Toast.LENGTH_LONG, true).show();
                                break;
                            case R.id.menu_about:
                                mDrawerLayout.closeDrawer(Gravity.START);
                                break;
                        }
                        return true;
                    }
                }
        );
    }
}
