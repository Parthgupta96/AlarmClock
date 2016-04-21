package com.example.parth.alarmclock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
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

    public static TimePicker time;
    static int hours;
    static int min;
    static Switch vibrationSwitch;
    static EditText alarmLabel;
    static Button ringtonePathChanger;
    static Spinner difficultySpinner;
    public final String LOG_TAG = MainActivity.class.getSimpleName();
    public AlarmDatabase alarmDatabase;
    public ArrayList<Alarm> activeAlarm;
    int count = 0;
    int id = 0;
    AlarmManager alarmManager;
    TimeSelection selectTime;
    static CoordinatorLayout coordinatorLayout;
    TextView addAlarm;
    ListView listView;
    String text = "";
    Cursor cursor;
    Alarm alarm, currentAlarm;
    Uri ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
    int isActiveIndex;
    int isVibrateIndex;
    int HourIndex;
    int MinIndex;
    int AlarmNameIndex;
    int RingtonePathIndex;
    int idIndex;
    Boolean noAlarm = true;
    int DifficultyIndex;
    alarmAdapter mAlarmAdapter;
    private PendingIntent pendingIntent;
    private ArrayList<Alarm> alarmsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        alarmsList = new ArrayList<>();
        activeAlarm = new ArrayList<>();
        addAlarm = (TextView) findViewById(R.id.AddAlarm);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorView);
        listView = (ListView) findViewById(R.id.ListView);

        mAlarmAdapter = new alarmAdapter(this, R.layout.alarm_list_view, R.id.alarmTime, alarmsList);
        listView.setAdapter(mAlarmAdapter);

        alarmDatabase = new AlarmDatabase(this);
        //cursor.moveToFirst();
        //updateListView();
        //       alarmsList = alarmDatabase.getAllAlarms();

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
                Intent intent = new Intent(getApplicationContext(), EditAlarm.class);
                intent.putExtra("alarm", alarmsList.get(position));
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int t=i;
                AlertDialog.Builder delete = new AlertDialog.Builder(MainActivity.this);
                delete.setMessage("Are you sure you want to Delete the alarm?");

                delete.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete();
                        //alarm.scheduleAlarm(getApplicationContext(), false);
                        alarmDatabase.deleteAlarm(alarmsList.get(t));
                        updateListView();
                        scheduleNextAlarm();
                        dialog.cancel();

                    }
                });
                delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                    }
                });
                delete.show();

                return true;
            }
        });
    }

    public void ringtonePath(View view) {
        final Uri currentTone = RingtoneManager.getActualDefaultRingtoneUri(MainActivity.this, RingtoneManager.TYPE_ALARM);
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
// else if(resultCode == 1){
//            Log.v(LOG_TAG, "List view updated");
//            Intent refresh = new Intent(this, MainActivity.class);
//            startActivity(refresh);
//            this.finish();
//        }
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

        cursor = alarmDatabase.getCursor();
//        cursor.moveToFirst();
        if (cursor.moveToLast() == false) {
            id = 1;
        } else {
            int idTable = cursor.getInt(0);
            id = idTable + 1;
        }
        alarm.setCalendar(hours, min);
       // alarm.setCoordinatorLayout(coordinatorLayout);
        alarm.showSnackbar();
        alarm.setAlarmName(text);
        alarm.setTimeInString();
        alarm.setIsVibrate(vibrationSwitch.isChecked());
        alarm.setIsActive(Boolean.TRUE);
        alarm.setRingtonePath(ringtone.toString());
        alarm.setDifficulty(Alarm.Difficulty.valueOf(difficultySpinner.getSelectedItem().toString()));
        alarm.setAlarmId(id);

        //database
        alarmDatabase.insertToDB(alarm);



//        Intent refresh = new Intent(this, MainActivity.class);
//        startActivity(refresh);
//        this.finish();


        Log.v(LOG_TAG, "After ok pressed value of hour " + hours + min);
        scheduleNextAlarm();
        //alarm.scheduleAlarm(this,true);

//        Intent myIntent = new Intent(this, AlarmReceiver.class);
//        myIntent.putExtra("alarm", alarm);
//        count++;
//        pendingIntent = PendingIntent.getBroadcast(this, (int) alarm.getMilliseconds(), myIntent, 0);
//        Log.v(LOG_TAG, "count: " + count);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getCalendar().getTimeInMillis(), pendingIntent);
//        cursor = alarmDataba.this);
//        Alarm alarm = getNext();
//        if(alarm != null)
        //alarm.scheduleAlarm(getApplicationContext());

        // alarmDatabase.viewData(this);

        //        alarmsList.add(alarm);
        //to arrange when new alarm is added

        updateListView();
        selectTime.dismiss();

    }

    public void addNewAlarm() {
        selectTime = new TimeSelection();
        selectTime.show(getFragmentManager(), "Time Picker");
    }

    public void updateListView() {
        mAlarmAdapter.notifyDataSetChanged();
        alarmsList.clear();

//        ArrayList<Alarm> alarmsList2 = new ArrayList<>();
        cursor = alarmDatabase.sortQuery();
        //cursor.moveToFirst();


            while (cursor.moveToNext()) {
                if (noAlarm) {
                    addAlarm.setVisibility(View.GONE);
                    noAlarm = false;
                }
                alarm = new Alarm();
                isActiveIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
                isVibrateIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISVIBRATE);
                HourIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_HOUR);
                MinIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_MIN);
                AlarmNameIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ALARMNAME);
                DifficultyIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_DIFFICULTY);
                RingtonePathIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_RINGTONEPATH);
                idIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_UID);

                alarm.setIsActive(1 == cursor.getInt(isActiveIndex));
                alarm.setIsVibrate(1 == cursor.getInt(isVibrateIndex));
                alarm.setHour(cursor.getInt(HourIndex));
                alarm.setMin(cursor.getInt(MinIndex));
                alarm.setAlarmName(cursor.getString(AlarmNameIndex));
                alarm.setRingtonePath(cursor.getString(RingtonePathIndex));
                alarm.setDifficulty(Alarm.Difficulty.values()[cursor.getInt(DifficultyIndex)]);
                alarm.setTimeInString();
                //alarm.setCoordinatorLayout(coordinatorLayout);
                alarm.setAlarmId(cursor.getInt(idIndex));
                alarm.setCalendar(alarm.getHour(),alarm.getMin());
                alarmsList.add(alarm);
                mAlarmAdapter.notifyDataSetChanged();

            }
        if(alarmsList.isEmpty()){
            addAlarm.setVisibility(View.VISIBLE);
        }

//        alarmsList = alarmsList2;
    }

    public void scheduleNextAlarm(){
        int flag = 0;
        cursor = alarmDatabase.sortQuery();
        while(cursor.moveToNext()){
            currentAlarm = alarmDatabase.getAlarm(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COLUMN_UID)));
            if(currentAlarm.getIsActive() == true){
                currentAlarm.scheduleAlarm(this,true);
                flag = 1;
                break;
            }
        }
        if(flag == 0){
            currentAlarm.scheduleAlarm(this, false);
        }
    }

    protected void closeAlarm(View view) {

    }

    public static CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }


    @Override
    protected void onPause() {
        super.onPause();
        alarmsList.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateListView();

    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        mAlarmAdapter.notifyDataSetChanged();
////        updateListView();
////        Intent refresh = new Intent(this, MainActivity.class);
////        startActivity(refresh);
////        this.finish();
//    }

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
