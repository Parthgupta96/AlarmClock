package com.example.parth.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static TimePicker time;
    static int hours;
    static int min;
    static Switch vibrationSwitch;
    static EditText alarmLabel;
    public final String LOG_TAG = MainActivity.class.getSimpleName();
    AlarmManager alarmManager;
    TimeSelection selectTime;
    CoordinatorLayout coordinatorLayout;
    TextView addAlarm;
    ListView listView;
    String text = "";
    Cursor cursor;
    AlarmDatabase alarmDatabase;
    Alarm alarm;


    int isActiveIndex ;
    int isVibrateIndex ;
    int HourIndex ;
    int MinIndex ;
    int AlarmNameIndex;
    int RingtonePathIndex;


    alarmAdapter mAlarmAdapter;
    private PendingIntent pendingIntent;
    private ArrayList<Alarm> alarmsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmsList = new ArrayList<>();

//        AlarmList = new ArrayList<>();
//        AlarmNameList = new ArrayList<>();

        addAlarm = (TextView) findViewById(R.id.AddAlarm);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorView);
        listView = (ListView) findViewById(R.id.ListView);

        mAlarmAdapter = new alarmAdapter(this, R.layout.alarm_list_view, R.id.alarmTime,alarmsList);
        listView.setAdapter(mAlarmAdapter);

        alarmDatabase = new AlarmDatabase(this);
        cursor = alarmDatabase.getCursor();

        if(cursor!=null){
            while(cursor.moveToNext()){
                alarm = new Alarm();
                 isActiveIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
                 isVibrateIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
                 HourIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
                 MinIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
                 AlarmNameIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
                 RingtonePathIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);

                alarm.setIsActive(1==cursor.getInt(isActiveIndex));
                alarm.setIsVibrate(1==cursor.getInt(isVibrateIndex));
                alarm.setHour(cursor.getInt(HourIndex));
                alarm.setMin(cursor.getInt(MinIndex));
                alarm.setAlarmName(cursor.getString(AlarmNameIndex));
                alarm.setRingtonePath(cursor.getString(RingtonePathIndex));
                alarm.setTimeInString();
            }
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewAlarm();
            }
        });



    }


    public void cancel(View view) {

        selectTime.dismiss();
    }


    public void addAlarm(View view) {
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        addAlarm.setVisibility(View.GONE);
        alarm = new Alarm();

        //get entered Time
        hours = time.getCurrentHour();
        min = time.getCurrentMinute();
        text = alarmLabel.getText().toString();
        Log.v(LOG_TAG, "after alarm name set");

        alarm.setAlarm(hours, min, coordinatorLayout);
        alarm.setAlarmName(text);
        alarm.setTimeInString();
        alarm.setIsVibrate(vibrationSwitch.isChecked());
        alarm.setIsActive(Boolean.TRUE);
        //alarm.setRingtonePath();

        mAlarmAdapter.notifyDataSetChanged();
        alarmsList.add(alarm);


        Log.v(LOG_TAG, "After ok pressed value of hour " + hours + min);

        //database
           // alarmDatabase.insertToDB(alarm);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), pendingIntent);
        alarmDatabase.viewData(this);
        selectTime.dismiss();

    }

    public void addNewAlarm() {
        selectTime = new TimeSelection();
        selectTime.show(getFragmentManager(), "Time Picker");
    }

    protected void closeAlarm(View view) {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Nothing Here!!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
