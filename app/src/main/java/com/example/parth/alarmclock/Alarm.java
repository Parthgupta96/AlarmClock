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

public class Alarm implements Serializable {


    public final String LOG_TAG = Alarm.class.getSimpleName();

    private Calendar calendar;
    private Boolean isActive = true;
    private Boolean isVibrate = true;
    private int alarmId;
    private int hour;
    private int min;
    //CoordinatorLayout coordinatorLayout;
    private long milliseconds;
    private String alarmName;
    private String ringtonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();
    private String timeInString;
    private String snackbarDialog;
    private int timeDifference[] = new int[2];
    private Difficulty difficulty = Difficulty.Easy;

    Boolean getIsActive() {
        return isActive;
    }

    void setIsActive(Boolean status) {
        isActive = status;
    }

    int getHour() {
        return hour;
    }

    void setHour(int hour) {
        this.hour = hour;
    }

    int getMin() {
        return min;
    }

    void setMin(int min) {
        this.min = min;
    }

    Boolean getIsVibrate() {
        return isVibrate;
    }

    void setIsVibrate(boolean state) {
        isVibrate = state;
    }

    int getAlarmId() {
        return alarmId;
    }

    void setAlarmId(int id) {
        //String string = "" + id;
        this.alarmId = id;
    }

    String getAlarmName() {
        return alarmName;
    }

    void setAlarmName(String name) {
        this.alarmName = name;
        Log.v(LOG_TAG, "alarm name set" + alarmName);
    }

    String getRingtonePath() {
        return ringtonePath;
    }

    void setRingtonePath(String path) {
        ringtonePath = path;
    }

    String getTimeInString() {
        return timeInString;
    }

    Calendar getCalendar() {
        return calendar;
    }

    Difficulty getDifficulty() {
        return difficulty;
    }

    void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    long getMilliseconds() {
        return milliseconds;
    }

    void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    void setIsActive(boolean active) {
        isActive = active;
    }

    void setTimeInString() {
        if (hour > 9) {
            if (min > 9)
                timeInString = hour + " : " + min;
            else {
                timeInString = hour + " : 0" + min;
            }
        } else {
            if (min > 9)
                timeInString = "0" + hour + " : " + min;
            else {
                timeInString = "0" + hour + " : 0" + min;
            }
        }
    }

//    public void setCoordinatorLayout(CoordinatorLayout coordinatorLayout) {
//        this.coordinatorLayout = coordinatorLayout;
//    }

    void setMilliseconds(int hour, int min) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 000);
        milliseconds = calendar.getTimeInMillis();

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
            timeDifference[0] += 24;
            this.calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

    }

    void showSnackbar() {
        CoordinatorLayout coordinatorLayout = MainActivity.getCoordinatorLayout();
        snackbarDialog = "Alarm set for ";
        calcTimeDifference(hour, min);

        snackbarDialog = snackbarDialog + timeDifference[0] + " Hours and " + timeDifference[1] + " Minutes";

        Snackbar.make(coordinatorLayout, snackbarDialog, Snackbar.LENGTH_LONG).show();
        timeDifference[0] = timeDifference[1] = 0;
    }

    void setCalendar(int hour, int min) {
        this.hour = hour;
        this.min = min;
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 000);

        milliseconds = calendar.getTimeInMillis();
        setMilliseconds(milliseconds);
        Log.v(LOG_TAG, "" + milliseconds);
    }

    void scheduleAlarm(Context context, boolean schedule) {
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        setCalendar(getHour(), getMin());
        calcTimeDifference(getHour(), getMin());
        myIntent.putExtra("alarm", this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,  0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (schedule) {
            //showSnackbar();
            Log.v(LOG_TAG,"received context " + context);
            alarmManager.set(AlarmManager.RTC_WAKEUP, this.getCalendar().getTimeInMillis(), pendingIntent);
        }else {
            alarmManager.cancel(pendingIntent);
        }

    }

    void scheduleAlarm(Context context) {
        showSnackbar();
        Intent myIntent = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        myIntent.putExtra("alarm", this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) this.getCalendar().getTimeInMillis(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, this.getCalendar().getTimeInMillis(), pendingIntent);
    }

    void cancelAlarm(Context context) {

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        myIntent.putExtra("alarm", this);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) this.getCalendar().getTimeInMillis(), myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
    }

    enum Difficulty {
        Easy,
        Medium,
        Hard;

        @Override
        public String toString() {
            switch (this.ordinal()) {
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

}
