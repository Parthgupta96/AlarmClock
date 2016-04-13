package com.example.parth.alarmclock;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;


public class TimeSelection extends DialogFragment{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.time_select, container, false);
        MainActivity.time = (TimePicker)rootView.findViewById(R.id.timePicker);
        MainActivity.vibrationSwitch = (Switch)rootView.findViewById(R.id.vibrateButton);
        MainActivity.alarmLabel = (EditText)rootView.findViewById(R.id.alarmName);
        MainActivity.ringtonePathChanger = (Button)rootView.findViewById((R.id.ringtonePathPicker));

        return rootView;


    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}