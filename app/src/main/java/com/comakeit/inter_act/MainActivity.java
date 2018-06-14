package com.comakeit.inter_act;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity {
    private RadioGroup mRadioGroup;
    private TextSwitcher mDescriptionTextSwitcher;
    private Button mSendButton;
    private EditText mDescriptionEditText;
    private EditText mRecipientEditText;
    private EditText mEventEditText;
    private Spinner mEventSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String eventName = "";

        mSendButton = findViewById(R.id.main_send_button);
        mDescriptionEditText = findViewById(R.id.descriptionEditText);
        mRecipientEditText = findViewById(R.id.recipientEditText);
        mEventEditText = findViewById(R.id.eventNameEditText);
        mEventSpinner = findViewById(R.id.eventSpinner);

        //Adapter for Spinner
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter.createFromResource(this, R.array.event_types, android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if(adapterView.getItemAtPosition(pos).toString().equals("Custom Event")){
                    mEventEditText.setVisibility(View.VISIBLE);
                }
                else
                    mEventEditText.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        mEventSpinner.setAdapter(eventAdapter);

        mRadioGroup = findViewById(R.id.iaTypeRadioGroup);
        mRadioGroup.clearCheck();

        mDescriptionTextSwitcher = findViewById(R.id.descriptionTextSwitcher);
        mDescriptionTextSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
                                                @Override
                                                public View makeView() {
                                                    TextView textView = new TextView(MainActivity.this);
                                                    textView.setGravity(Gravity.TOP);
                                                    textView.setPadding(5,5,5,5);
                                                    textView.setTextSize(20);
                                                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                                                    return textView;
                                                }
                                            });
                mDescriptionTextSwitcher.setInAnimation(getApplicationContext(), android.R.anim.slide_in_left);
        mDescriptionTextSwitcher.setOutAnimation(getApplicationContext(), android.R.anim.slide_out_right);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton rb = findViewById(i);
                if(rb != null){
                    mDescriptionTextSwitcher.setText(rb.getText());
                    mDescriptionEditText.setVisibility(View.VISIBLE);
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mRadioGroup.getCheckedRadioButtonId() == -1)
                    Toast.makeText(MainActivity.this, "Please select FEEDBACK or APPRECIATION.", Toast.LENGTH_SHORT).show();
                else if(mDescriptionEditText.getText().toString().trim().length()==0)
                    Toast.makeText(MainActivity.this, "Feedback/Appreciation can't be left blank", Toast.LENGTH_SHORT).show();
                else if(mRecipientEditText.getText().toString().trim().length()==0)
                    Toast.makeText(MainActivity.this, "Please enter the RECIPIENT", Toast.LENGTH_SHORT).show();
                else{
                    String TO = mRecipientEditText.getText().toString();
                    RadioButton rb = findViewById(mRadioGroup.getCheckedRadioButtonId());
                    String type = rb.getText().toString();
                    String event;
                    if(mEventEditText.getVisibility()==View.VISIBLE)
                        event = "\"" + mEventEditText.getText().toString() + "\"";
                    else
                        event = "\"" + mEventSpinner.getSelectedItem().toString() + "\"";
//                    String event = "\"" + eventName + "\"";
                    String desc = mDescriptionEditText.getText().toString();
                    sendEmail(TO, type, event, desc);
                }
            }
        });

    }

    protected void sendEmail(String TO, String iatype, String event, String description){
        Log.i("Send Email","Trying to send email...");
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        String message = description;       //Currently, the message is same as description but that will probably change soon...
//        String recipient = "mailto:"+TO;
//        Log.i("Send email", recipient);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{TO});
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,iatype+" regarding "+event);
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        try{
            startActivity(Intent.createChooser(emailIntent, "Send Interaction..."));
            finish();
            Log.i("Send email", "Email sent!");
        }catch (android.content.ActivityNotFoundException ex){
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }
}
