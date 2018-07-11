package com.comakeit.inter_act;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

//TODO Fragment isn't completely modular YET. Change the way info is passed back to activity in future for more reusable form

public class DateTimePickerFragment extends android.support.v4.app.Fragment {
    private TextView dateTextView, timeTextView;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final Calendar mCalendar = Calendar.getInstance();
    OnDataPass dataPasser;

    public interface OnDataPass{
        /** Interface which must be implemented by every class using this fragment to retrieve the date and time set inside the fragment
         *
         * @param data Calendar containing date/time
         */
        void onDataPass(Calendar data);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_date_time_picker, container, false);

        dateTextView = view.findViewById(R.id.fragment_date_text_view);
        timeTextView = view.findViewById(R.id.fragment_time_text_view);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch Date picker dialog
                datePicker();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker();
            }
        });

        return view;
    }

    private void datePicker() {
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format("%02d",dayOfMonth) + "-" + String.format("%02d",monthOfYear + 1) + "-" + year;
                dateTextView.setText(date);
                mCalendar.set(year,monthOfYear,dayOfMonth);
                timePicker();
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void timePicker() {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String display = String.format("%02d",hourOfDay) + ":" + String.format("%02d",minute);
                        timeTextView.setText(display);     //TODO Fix warning. Priority: Low
                        mCalendar.set(Calendar.HOUR, hourOfDay);
                        mCalendar.set(Calendar.MINUTE, minute);
                        dataPasser.onDataPass(mCalendar);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

}
