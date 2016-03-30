package com.example.parth.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static TimePicker time;
    static int hours;
    static int min;
    static Button OkButton;
    public final String LOG_TAG = MainActivity.class.getSimpleName();
    AlarmManager alarmManager;
    TimeSelection selectTime;
    CoordinatorLayout coordinatorLayout;
    TextView addAlarm;
    ListView listView;
    String string;

    alarmAdapter mAlarmAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private PendingIntent pendingIntent;
    private FrameLayout frameLayout;
    private ArrayList<String> AlarmList;

    //TODO CHANGE FRAME TO COORDINATOR
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mRecyclerView = (RecyclerView) findViewById(R.id.alarm_list);
//        mRecyclerView.setHasFixedSize(true);
      //  mLayoutManager = new LinearLayoutManager(this);
        AlarmList = new ArrayList<>();
         string ="hi";
      //  mRecyclerView.setLayoutManager(mLayoutManager);
        //mAdapter = new alarmAdapter();
        addAlarm = (TextView)findViewById(R.id.AddAlarm);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorView);
        // frameLayout = (FrameLayout)findViewById(R.id.coordinatorView);
        listView = (ListView) findViewById(R.id.ListView);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
        AlarmList.add(hours + ":" + min);
        addAlarm.setVisibility(View.GONE);

        mAlarmAdapter = new alarmAdapter(this, R.layout.alarm_list_view, string);
        listView.setAdapter(mAlarmAdapter);

        Log.v(LOG_TAG, "After ok pressed value of hour " + hours + min);
        String dialog = "Alarm Set for ";
        selectTime.dismiss();
        Calendar calendar = Calendar.getInstance();
        // Log.v(LOG_TAG,"current time"+calendar.get(Calendar.HOUR_OF_DAY)+" "+calendar.get(Calendar.MINUTE));
        int MinDiff = min - calendar.get(Calendar.MINUTE);
        int HourDiff = 0;
        if (MinDiff < 0) {
            MinDiff += 60;
            HourDiff = -1;
        }
        HourDiff = HourDiff + hours - calendar.get(Calendar.HOUR_OF_DAY);
        if (HourDiff < 0) {//TODO change for next day
            HourDiff = 12 + HourDiff;
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }

        dialog = dialog + HourDiff + " Hours and " + MinDiff + " Minutes";
        Snackbar.make(coordinatorLayout, dialog, Snackbar.LENGTH_LONG).show();
        //Toast.makeText(this, dialog, Toast.LENGTH_SHORT).show();

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 000);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        Log.v(LOG_TAG, "Setting alarm");
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public void addNewAlarm() {
        selectTime = new TimeSelection();
        selectTime.show(getFragmentManager(), "Time Picker");
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
