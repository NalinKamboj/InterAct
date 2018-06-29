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
        /** Interface which must be implemented by ever class using this fragment to retrieve the date and time set in the fragment
         *
         * @param data String containing date/time
         * @param type 0 for TIME, 1 for DATE
         */
        void onDataPass(String data, int type);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_date_time_picker, container, false);

        dateTextView = view.findViewById(R.id.fragment_date_text_view);
        timeTextView = view.findViewById(R.id.fragment_time_text_view);

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);
        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Launch Date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = i2 + "-" + (i1 + 1) + "-" + i;
                        dateTextView.setText(date);
                        mCalendar.set(i,i1,i2);
                        dataPasser.onDataPass(date, 1);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String display = String.format("%02d",i) + ":" + String.format("%02d",i1);
                        timeTextView.setText(display);     //TODO Fix warning. Priority: Low
                        mCalendar.set(Calendar.HOUR, i);
                        mCalendar.set(Calendar.MINUTE, i1);
                        dataPasser.onDataPass(display, 0);
                    }
                }, mHour, mMinute, true);
                timePickerDialog.show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }

}
