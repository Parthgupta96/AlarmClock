package com.example.parth.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static TimePicker time;
    static int hours;
    static int min;
    static int miliseconds;
    static Button OkButton;
    public final String LOG_TAG = MainActivity.class.getSimpleName();
    AlarmManager alarmManager;
    TimeSelection selectTime;
    CoordinatorLayout coordinatorLayout;
    TextView addAlarm;
    ListView listView;
    static Switch vibrationSwitch;
    static EditText alarmLabel;
    String text = "";

    Alarm alarm = new Alarm();

    alarmAdapter mAlarmAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private PendingIntent pendingIntent;
    private ArrayList<String> AlarmList;
    private ArrayList<String> AlarmNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmList = new ArrayList<>();
        AlarmNameList = new ArrayList<>();
        addAlarm = (TextView)findViewById(R.id.AddAlarm);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorView);
        listView = (ListView) findViewById(R.id.ListView);
        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);



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


        hours = time.getCurrentHour();
        min = time.getCurrentMinute();

        alarm.setAlarm(hours, min, coordinatorLayout);

        text = alarmLabel.getText().toString();
        alarm.setAlarmName(text);
        Log.v(LOG_TAG, "after alarm name set");
        alarm.setTimeInString();
        AlarmList.add(alarm.getTimeInString());
        AlarmNameList.add(alarm.getAlarmName());

        addAlarm.setVisibility(View.GONE);

        mAlarmAdapter = new alarmAdapter(this, R.layout.alarm_list_view, R.id.alarmTime, AlarmList, AlarmNameList);
        listView.setAdapter(mAlarmAdapter);

        Log.v(LOG_TAG, "After ok pressed value of hour " + hours + min);
        selectTime.dismiss();

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        Log.v(LOG_TAG, "Setting alarm");
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), pendingIntent);

        alarm.setIsVibrate(vibrationSwitch.isChecked());
    }

    public void addNewAlarm() {
        selectTime = new TimeSelection();
        selectTime.show(getFragmentManager(), "Time Picker");
    }

    protected void closeAlarm(View view){


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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
