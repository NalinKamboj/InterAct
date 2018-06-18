package com.comakeit.inter_act;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

//TODO Fragment isn't completely modular YET. Change the way info is passed back to activity in future for more reusable form

public class DateTimePickerFragment extends Fragment {
    private TextView dateTextView, timeTextView;
    private int mYear, mMonth, mDay, mHour, mMinute;
    final Calendar mCalendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
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
                        dateTextView.setText(i2 + "-" + (i1 + 1) + "-" + i);
                        mCalendar.set(i,i1,i2);
                        ((MainActivity)getActivity()).setDate(mCalendar);
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
                        ((MainActivity)getActivity()).setDate(mCalendar);
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
        //Checking if hosting activity implements necessary functionality
//        if(!(context instanceof View.OnClickListener)){
//            throw new ClassCastException(context.toString() + " must implement OnItemClickListener");
//        }
    }

}
