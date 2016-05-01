package com.example.parth.alarmclock;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class EditAlarm extends AppCompatActivity {

    static ArrayList<String> difficultyList;
    static ArrayAdapter<String> difficultyLevelAdapter;
    Alarm alarm, currentAlarm;
    TextView ringtone;
    Uri newRingtoneUri;
    TextView AlarmLabel;
    TextView alarmTime;
    Switch vibrate;
    Spinner difficultySpinner;
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
    int oldDifficultyInt;
    int newDifficultyInt;
    String newAlarmLabel;
    String newTime;
    String newRingtonePath;
    String newRingtoneLabel;
    Boolean newIsVibrate;
    Boolean changes = false;
    Alarm.Difficulty oldDifficulty;
    Alarm.Difficulty newDifficulty;
    AlarmDatabase alarmDatabase;
    ViewGroup edit;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.time_shared_transition));
            getWindow().setSharedElementExitTransition(null);
            getWindow().setSharedElementReturnTransition(null);
            getWindow().setEnterTransition(TransitionInflater.from(this).inflateTransition(R.transition.edit_alarm_enter));
            getWindow().setAllowReturnTransitionOverlap(false);
            getWindow().setExitTransition(TransitionInflater.from(this).inflateTransition(R.transition.edit_alarm_exit));
//   getWindow().setReturnTransition(null);
            //getWindow().setSharedElementReenterTransition(null);
        }
        setContentView(R.layout.activity_edit_alarm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        alarmDatabase = new AlarmDatabase(this);
        alarm = (Alarm) getIntent().getSerializableExtra("alarm");
        Log.v("Recd alarmin edit alarm", "id: " + alarm.getAlarmId());

        difficultyList = new ArrayList<>();
        difficultyList.add("Easy");
        difficultyList.add("Medium");
        difficultyList.add("Hard");
        difficultyLevelAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficultyList);
        difficultyLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        newAlarmLabel = oldAlarmLabel = alarm.getAlarmName();
        newTime = oldTime = alarm.getTimeInString();
        newHour = alarm.getHour();
        newMin = alarm.getMin();
        newIsVibrate = oldIsVibrate = alarm.getIsVibrate();
        newRingtonePath = oldRingtonePath = alarm.getRingtonePath();
        oldRingtone = RingtoneManager.getRingtone(this, Uri.parse(oldRingtonePath));
        oldRingtoneLabel = oldRingtone.getTitle(this);
        oldDifficulty = alarm.getDifficulty();
        newDifficultyInt = oldDifficultyInt = oldDifficulty.ordinal();

        edit = (ViewGroup) findViewById(R.id.EditViewGroup);
        AlarmLabel = (TextView) findViewById(R.id.EditAlarmLabel);
        vibrate = (Switch) findViewById(R.id.EditVibrate);
        alarmTime = (TextView) findViewById(R.id.EditAlarmTime);
        ringtone = (TextView) findViewById(R.id.EditRingtone);
        difficultySpinner = (Spinner) findViewById(R.id.editDifficultyLevel);

        difficultySpinner.setAdapter(difficultyLevelAdapter);

        difficultySpinner.setSelection(oldDifficultyInt);

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
                        if (!changes && !newAlarmLabel.equals(oldAlarmLabel)) {
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
                        if (!changes && !oldTime.equals(newTime)) {
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
                if (!changes && oldIsVibrate != newIsVibrate) {
                    changes = true;
                }
            }
        });

        ringtone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectRingtone();
            }
        });

        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newDifficulty = Alarm.Difficulty.valueOf(parent.getItemAtPosition(position).toString());

                newDifficultyInt = newDifficulty.ordinal();
                if (newDifficultyInt != oldDifficultyInt) {
                    changes = true;
                    //difficultySpinner.setSelection(newDifficultyInt);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            if (!newRingtonePath.equals(oldRingtonePath)) {
                changes = true;
            }
            newRingtone = RingtoneManager.getRingtone(this, Uri.parse(newRingtonePath));
            newRingtoneLabel = newRingtone.getTitle(this);
            ringtone.setText(newRingtoneLabel);
            oldRingtonePath = newRingtonePath;

        }
    }

    void saveChanges() {
        //alarm.scheduleAlarm(getApplicationContext(),false);
        alarm.setAlarmName(newAlarmLabel);
        alarm.setHour(newHour);
        alarm.setMin(newMin);
        alarm.setIsActive(true);
        alarm.setTimeInString();
        alarm.calcTimeDifference(newHour, newMin);
        alarm.showSnackbar();
        alarm.setCalendar(newHour, newMin);
        alarm.setIsVibrate(newIsVibrate);
        alarm.setRingtonePath(newRingtonePath);
        alarm.setMilliseconds(newHour, newMin);
        alarm.setDifficulty(newDifficulty);
        Log.v("in edit alarm", "id: " + alarm.getAlarmId());
        alarmDatabase.updateData(alarm);
        cursor = alarmDatabase.sortQuery();
        while (cursor.moveToNext()) {
            currentAlarm = alarmDatabase.getAlarm(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COLUMN_UID)));
            if (currentAlarm.getIsActive() == true) {
                currentAlarm.scheduleAlarm(this, true);
                break;
            }
        }
        //alarm.scheduleAlarm(getApplicationContext(),true);
        //setResult(1, null);
        // this.finish();
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
                timeInString += hour + " : " + min;
            else {
                timeInString += hour + " : 0" + min;
            }
        }
        return timeInString;
    }


    public void removeViewGroup(){
        if(Build.VERSION.SDK_INT>=21){
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.TOP);
            slide.setDuration(300);
            TransitionManager.beginDelayedTransition(edit,slide);
            edit.setVisibility(View.GONE);
        }
    }

    public void showExitDialog() {
        final AlertDialog.Builder exitConfirm = new AlertDialog.Builder(EditAlarm.this);
        exitConfirm.setMessage("Save Changes ?");

        exitConfirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveChanges();
                removeViewGroup();
                supportFinishAfterTransition();
            }
        });
        exitConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                removeViewGroup();
                finish();
            }
        });
        exitConfirm.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
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
        if (changes) {
            showExitDialog();
        } else {
            removeViewGroup();
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return (true);
        }
        return (super.onOptionsItemSelected(item));
    }
}