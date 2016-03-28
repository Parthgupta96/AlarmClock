package com.example.parth.alarmclock;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;


public class TimeSelection extends DialogFragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.time_select, container, false);

        MainActivity.time = (TimePicker)rootView.findViewById(R.id.timePicker);
        MainActivity.OkButton =(Button)rootView.findViewById(R.id.okButton);
        MainActivity.hours = MainActivity.time.getCurrentHour();
        MainActivity.min = MainActivity.time.getCurrentMinute();


        return rootView;


    }
}