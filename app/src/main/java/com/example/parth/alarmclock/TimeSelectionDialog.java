package com.example.parth.alarmclock;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;

import java.util.Arrays;
import java.util.List;



public class TimeSelectionDialog extends DialogFragment implements View.OnClickListener{

    @Nullable
    private CallBackListener listener;
    private Switch vibrationSwitch;
    private Spinner difficultySpinner;
    private EditText alarmLabel;
    private TimePicker timePicker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.time_select, container, false);
         timePicker = rootView.findViewById(R.id.timePicker);
        vibrationSwitch = rootView.findViewById(R.id.vibrateButton);
         alarmLabel = rootView.findViewById(R.id.alarmName);
        difficultySpinner = rootView.findViewById(R.id.difficultyLevel);

        Button cancelButton = rootView.findViewById(R.id.cancelButton);
        Button okButton = rootView.findViewById(R.id.okButton);
        Button ringtoneButton = rootView.findViewById(R.id.ringtonePathPicker);

        cancelButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        ringtoneButton.setOnClickListener(this);

        List<String> difficultyList = Arrays.asList("Easy","Medium","Hard");
        ArrayAdapter<String> difficultyLevelAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, difficultyList);
        difficultyLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyLevelAdapter);
        return rootView;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cancelButton:
                dismiss();
                break;
            case R.id.okButton:
                onOkClicked();
                break;
            case R.id.ringtonePathPicker:
                if (listener != null) listener.onChangeAlarmToneClicked();
                break;
        }
    }

    private void onOkClicked() {

        Alarm alarm = new Alarm();

        //get entered Time
        int hours = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();

        alarm.setCalendar(hours, min);
        alarm.showSnackbar();
        alarm.setAlarmName(alarmLabel.getText().toString().trim());
        alarm.setTimeInString();
        alarm.setIsVibrate(vibrationSwitch.isChecked());
        alarm.setIsActive(true);
        alarm.setDifficulty(Alarm.Difficulty.valueOf(difficultySpinner.getSelectedItem().toString()));
        if(listener!=null) listener.onOkPressed(alarm);
        dismiss();
    }

    public interface CallBackListener {
        void onOkPressed(Alarm alarm);

        void onChangeAlarmToneClicked();
    }

    public void setListener(@Nullable CallBackListener listener) {
        this.listener = listener;
    }
}