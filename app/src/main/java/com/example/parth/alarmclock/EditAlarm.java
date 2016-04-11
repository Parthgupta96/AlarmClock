package com.example.parth.alarmclock;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class EditAlarm extends AppCompatActivity {

    Alarm alarm;
    TextView ringtone;
    TextView AlarmLabel;
    TextView alarmTime;
    Switch vibrate;
    String oldAlarmLabel;
    String oldTime;
    String oldRingtone;
    Boolean oldIsVibrate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        alarm = (Alarm)getIntent().getSerializableExtra("alarm");
        oldAlarmLabel = alarm.getAlarmName();
        oldTime = alarm.getTimeInString();
        oldIsVibrate = alarm.getIsVibrate();
        oldRingtone = alarm.getRingtonePath();
        oldRingtone = oldRingtone.substring(oldRingtone.lastIndexOf('/')+1);

        AlarmLabel = (TextView)findViewById(R.id.EditAlarmLabel);
        vibrate = (Switch)findViewById(R.id.EditVibrate);
        alarmTime =(TextView)findViewById(R.id.EditAlarmTime);
        ringtone = (TextView)findViewById(R.id.EditRingtone);

        AlarmLabel.setText(oldAlarmLabel);
        alarmTime.setText(oldTime);
        vibrate.setChecked(oldIsVibrate);
        ringtone.setText(oldRingtone);
        AlarmLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditAlarm.this);
                dialog.setTitle("Alarm label");
                EditText label = new EditText(EditAlarm.this);
                label.setText(oldAlarmLabel);
                dialog.setView(label);
                dialog.show();

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
