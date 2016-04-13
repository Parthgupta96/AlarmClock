package com.example.parth.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int count = 0;

    public static TimePicker time;
    static int hours;
    static int min;
    static Switch vibrationSwitch;
    static EditText alarmLabel;
    static Button ringtonePathChanger;
    static Spinner difficultySpinner;
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
    Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

    int isActiveIndex ;
    int isVibrateIndex ;
    int HourIndex ;
    int MinIndex ;
    int AlarmNameIndex;
    int RingtonePathIndex;
    Boolean noAlarm=true;

    int DifficultyIndex;

    alarmAdapter mAlarmAdapter;
    private PendingIntent pendingIntent;
    private ArrayList<Alarm> alarmsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmsList = new ArrayList<>();

        addAlarm = (TextView) findViewById(R.id.AddAlarm);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorView);
        listView = (ListView) findViewById(R.id.ListView);

        mAlarmAdapter = new alarmAdapter(this, R.layout.alarm_list_view, R.id.alarmTime,alarmsList);
        listView.setAdapter(mAlarmAdapter);

        alarmDatabase = new AlarmDatabase(this);
        cursor = alarmDatabase.getCursor();

        if(cursor!=null){


            while(cursor.moveToNext()){
                if(noAlarm) {
                    addAlarm.setVisibility(View.GONE);
                    noAlarm = false;
                }
                alarm = new Alarm();

                isActiveIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
                isVibrateIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISVIBRATE);
                HourIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_HOUR);
                MinIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_MIN);
                AlarmNameIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ALARMNAME);
//                DifficultyIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_DIFFICULTY);
                RingtonePathIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_RINGTONEPATH);

                alarm.setIsActive(1 == cursor.getInt(isActiveIndex));
                alarm.setIsVibrate(1 == cursor.getInt(isVibrateIndex));
                alarm.setHour(cursor.getInt(HourIndex));
                alarm.setMin(cursor.getInt(MinIndex));
                alarm.setAlarmName(cursor.getString(AlarmNameIndex));
                alarm.setRingtonePath(cursor.getString(RingtonePathIndex));
//                alarm.setDifficulty(Alarm.Difficulty.values()[cursor.getInt(DifficultyIndex)]);
                alarm.setTimeInString();

                alarmsList.add(alarm);

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),EditAlarm.class);
                intent.putExtra("alarm",alarmsList.get(position));
                startActivity(intent);
            }
        });
    }

    public void ringtonePath(View view){
        final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_ALARM);
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        }
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
        alarm.setRingtonePath(ringtone.toString());
        alarm.setDifficulty(Alarm.Difficulty.valueOf(difficultySpinner.getSelectedItem().toString()));

        mAlarmAdapter.notifyDataSetChanged();
        alarmsList.add(alarm);


        Log.v(LOG_TAG, "After ok pressed value of hour " + hours + min);

        //database
            alarmDatabase.insertToDB(alarm);

        Intent myIntent = new Intent(this, AlarmReceiver.class);
        myIntent.putExtra("alarm", alarm);
        count++;
        pendingIntent = PendingIntent.getBroadcast(this, count, myIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), pendingIntent);
//        Alarm alarm = getNext();
//        if(alarm != null)
            //alarm.scheduleAlarm(getApplicationContext());

       // alarmDatabase.viewData(this);
        selectTime.dismiss();

    }

    public void addNewAlarm() {
        selectTime = new TimeSelection();
        //selectTime.requestWindowFeature(Window.FEATURE_NO_TITLE);

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
