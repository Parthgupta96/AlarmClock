package com.example.parth.alarmclock;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;


public class TimeSelection extends DialogFragment{

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.time_select, container, false);

        MainActivity.time = (TimePicker)rootView.findViewById(R.id.timePicker);
        MainActivity.vibrationSwitch = (Switch)rootView.findViewById(R.id.vibrateButton);
        MainActivity.alarmLabel = (EditText)rootView.findViewById(R.id.alarmName);

        return rootView;


    }
}