package com.example.parth.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Priyanshu on 30-03-2016.
 */

public class Alarm implements Serializable{


    public final String LOG_TAG = Alarm.class.getSimpleName();

    private Calendar calendar;
    private Boolean isActive = true;
    private Boolean isVibrate = true;
    private int alarmId;
    private int hour;
    private int min;
    private long miliseconds;
    private String alarmName;
    private String ringtonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
    private String timeInString;
    private String snackbarDialog;
    private int timeDifference[] = new int[2];
    enum Difficulty{
        Easy,
        Medium,
        Hard;

        @Override
        public String toString() {
            switch (this.ordinal()){
                case 0:
                    return "Easy";
                case 1:
                    return "Medium";
                case 2:
                    return "Hard";
            }
            return super.toString();
        }
    }
    private Difficulty difficulty = Difficulty.Easy;


    Boolean getIsActive() {
        return isActive;
    }

    int getHour(){return hour;}
    int getMin(){return min;}

    Boolean getIsVibrate() {
        return isVibrate;
    }

    int getAlarmId() {
        return alarmId;
    }

    String getAlarmName() {
        return alarmName;
    }

    String getRingtonePath() {
        return ringtonePath;
    }

    String getTimeInString() {
        return timeInString;
    }

    Calendar getCalendar() {
        return calendar;
    }

    Difficulty getDifficulty(){ return difficulty; }

    void setDifficulty(Difficulty difficulty){ this.difficulty = difficulty; }

    void setIsActive() {
        isActive = (!isActive);
    }

    void setIsActive(Boolean status) {
        isActive = status;
    }

    void setIsVibrate(boolean state) {
        isVibrate = state;
    }

    void setAlarmId(int id) {
        this.alarmId = id;
    }

    void setAlarmName(String name) {
        this.alarmName = name;
        Log.v(LOG_TAG, "alarm name set" + alarmName);
    }

    void setRingtonePath(String path) {
        ringtonePath = path;
    }

    void setHour(int hour){
        this.hour = hour;
    }

    void setMin(int min){
        this.min = min;
    }


    void setTimeInString() {
        if (min > 9)
            timeInString = hour + " : " + min;
        else {
            timeInString = hour + " : 0" + min;
        }
    }

    void calcTimeDifference(int alarmHr, int alarmMin) {
        //timeDifference[0] is hours
        Calendar calendar = Calendar.getInstance();
        int currentMin = calendar.get(Calendar.MINUTE);
        int currentHr = calendar.get(Calendar.HOUR_OF_DAY);
        timeDifference[1] = alarmMin - currentMin;

        if (timeDifference[1] < 0) {
            timeDifference[1] += 60;
            timeDifference[0] = -1;
        }
        timeDifference[0] += alarmHr - currentHr;

        if (currentHr > alarmHr || (currentHr == alarmHr && currentMin > alarmMin)) {//alarm for next day
            timeDifference[0] +=24 ;
            this.calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    void setAlarm(int hour, int min, CoordinatorLayout coordinatorLayout) {
        snackbarDialog = "Alarm set for ";
        this.hour = hour;
        this.min = min;
        calendar = Calendar.getInstance();
        calcTimeDifference(hour, min);

        this.snackbarDialog = this.snackbarDialog + timeDifference[0] + " Hours and " + timeDifference[1] + " Minutes";

        Snackbar.make(coordinatorLayout, this.snackbarDialog, Snackbar.LENGTH_LONG).show();
        timeDifference[0] = timeDifference[1] = 0;

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 000);

        miliseconds = calendar.getTimeInMillis();
        Log.v(LOG_TAG, "" + miliseconds);
        //Toast.makeText(, "" + miliseconds)

    }

    void  scheduleAlarm(Context context){
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        myIntent.putExtra("alarm", this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, this.getCalendar().getTimeInMillis(), pendingIntent);
    }

}
