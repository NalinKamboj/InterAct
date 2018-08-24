package com.comakeit.inter_act.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;
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


        ImageView nalinLinkedIn, nalinEmail, soumLinkedIn, soumEmail, abhiLinkedIn, abhiEmail;

        nalinEmail = findViewById(R.id.nalin_email_image);
        nalinLinkedIn = findViewById(R.id.nalin_linkedin_image);
        soumEmail = findViewById(R.id.soum_email_image);
        soumLinkedIn = findViewById(R.id.soum_linkedin_image);
        abhiLinkedIn = findViewById(R.id.abhi_linkedin_image);
        abhiEmail = findViewById(R.id.abhi_email_image);

        //TODO Add linkedIn and email icons and add respective click events
        nalinLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/nalin-kamboj-662118123/"));
                startActivity(intent);
            }
        });

        soumLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/soumyendu-banerjee-2487a415/"));
                startActivity(intent);
            }
        });

        abhiLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/abhinav3414/"));
                startActivity(intent);
            }
        });

        nalinEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("email", "nalin.1997@gmail.com");
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);
                Toasty.info(getApplicationContext(), "Email copied to clipboard", Toast.LENGTH_SHORT, true).show();
            }
        });

        soumEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("email", "soumyendu.b@comakeit.com");
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);
                Toasty.info(getApplicationContext(), "Email copied to clipboard", Toast.LENGTH_SHORT, true).show();
            }
        });

        abhiEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("email", "abhinav.g@comakeit.com");
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);
                Toasty.info(getApplicationContext(), "Email copied to clipboard", Toast.LENGTH_SHORT, true).show();
            }
        });
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
