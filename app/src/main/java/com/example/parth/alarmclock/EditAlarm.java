package com.example.parth.alarmclock;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

public class EditAlarm extends AppCompatActivity {

    Alarm alarm;
    TextView ringtone;
    Uri newRingtoneUri;
    TextView AlarmLabel;
    TextView alarmTime;
    Switch vibrate;
    Ringtone oldRingtone;
    Ringtone newRingtone;
    int newHour;
    int oldHour;
    int newMin;
    int oldMin;
    String oldAlarmLabel;
    String oldTime;
    String oldRingtonePath;
    String oldRingtoneLabel;
    String defaultLabel = "Alarm Label";
    Boolean oldIsVibrate;
    String newAlarmLabel;
    String newTime;
    String newRingtonePath;
    String newRingtoneLabel;
    Boolean newIsVibrate;
    Boolean changes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alarm = (Alarm) getIntent().getSerializableExtra("alarm");

        newAlarmLabel = oldAlarmLabel = alarm.getAlarmName();
        newTime = oldTime = alarm.getTimeInString();
        newHour = alarm.getHour();
        newMin = alarm.getMin();
        newIsVibrate = oldIsVibrate = alarm.getIsVibrate();
        newRingtonePath = oldRingtonePath = alarm.getRingtonePath();
        oldRingtone = RingtoneManager.getRingtone(this, Uri.parse(oldRingtonePath));
        oldRingtoneLabel = oldRingtone.getTitle(this);

        AlarmLabel = (TextView) findViewById(R.id.EditAlarmLabel);
        vibrate = (Switch) findViewById(R.id.EditVibrate);
        alarmTime = (TextView) findViewById(R.id.EditAlarmTime);
        ringtone = (TextView) findViewById(R.id.EditRingtone);

        if (oldAlarmLabel.equals("")) {
            AlarmLabel.setText(defaultLabel);
            AlarmLabel.setTextColor(getResources().getColor(R.color.dim_foreground_disabled_material_light));

        } else {
            AlarmLabel.setText(oldAlarmLabel);
        }
        alarmTime.setText(oldTime);
        vibrate.setChecked(oldIsVibrate);
        ringtone.setText(oldRingtoneLabel);


        AlarmLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditAlarm.this);
                dialog.setTitle("Alarm label");
                final EditText label = new EditText(EditAlarm.this);
                label.setText(oldAlarmLabel);
                label.setSingleLine(true);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newAlarmLabel = label.getText().toString();
                        if(!newAlarmLabel.equals(oldAlarmLabel)){
                            changes = true;
                        }
                        AlarmLabel.setText(newAlarmLabel);
                        oldAlarmLabel = newAlarmLabel;
                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialog.setView(label);
                dialog.show();

            }
        });

        alarmTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog dialog = new TimePickerDialog(EditAlarm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        newHour = hourOfDay;
                        newMin = minute;
                        newTime = getTimeInString(newHour, newMin);
                        alarmTime.setText(newTime);
                        if(!oldTime.equals(newTime)){
                           changes = true;
                        }
                    }
                }, alarm.getHour(), alarm.getMin(), false);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();

            }
        });

        vibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newIsVibrate = isChecked;
                if(oldIsVibrate!=newIsVibrate){
                    changes = true;
                }
            }
        });

        ringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectRingtone();


            }
        });


    }

    public void selectRingtone() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(oldRingtonePath));
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            newRingtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            newRingtonePath = newRingtoneUri.toString();
            if(!newRingtonePath.equals(oldRingtonePath)){
                changes = true;
            }
            newRingtone = RingtoneManager.getRingtone(this, Uri.parse(newRingtonePath));
            newRingtoneLabel = newRingtone.getTitle(this);
            ringtone.setText(newRingtoneLabel);
            oldRingtonePath = newRingtonePath;

        }
    }

    void saveChanges() {
        alarm.setAlarmName(newAlarmLabel);
        alarm.setHour(newHour);
        alarm.setMin(newMin);
        alarm.setTimeInString();
        alarm.setIsVibrate(newIsVibrate);
        alarm.setRingtonePath(newRingtonePath);

    }

    String getTimeInString(int hour, int min) {
        String timeInString;
        if (hour > 9) {
            if (min > 9)
                timeInString = hour + " : " + min;
            else {
                timeInString = hour + " : 0" + min;
            }
        } else {
            timeInString = "0";
            if (min > 9)
                timeInString = hour + " : " + min;
            else {
                timeInString = hour + " : 0" + min;
            }
        }
        return timeInString;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if(changes){
            final AlertDialog.Builder exitConfirm = new AlertDialog.Builder(EditAlarm.this);
            exitConfirm.setMessage("Save Changes ?");

            exitConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveChanges();
                    supportFinishAfterTransition();
                }
            });
            exitConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            exitConfirm.show();

        }else
        {
             super.onBackPressed();
        }


    }
}
