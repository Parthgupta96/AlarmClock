package com.example.parth.alarmclock;

import android.media.RingtoneManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Priyanshu on 30-03-2016.
 */
public class Alarm {

    public final String LOG_TAG = MainActivity.class.getSimpleName();

    private Calendar calendar;
    private Boolean isActive = true;
    private Boolean isVibrate = true;
    private int alarmId;
    private int hour;
    private int min;
    private long miliseconds;
    private String alarmName;
    private String ringtonePath = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString();;
    private String timeInString;
    private String snackbarDialog = "Alarm set for ";


    Boolean getIsActive(){
        return isActive;
    }

    Boolean getIsVibrate(){
        return isVibrate;
    }

    int getAlarmId(){
        return alarmId;
    }

    String getAlarmName(){
        return alarmName;
    }

    String getRingtonePath(){
        return ringtonePath;
    }

    String getTimeInString(){
        return timeInString;
    }

    Calendar getCalendar(){
        return calendar;
    }

    void setIsActive(){
        isActive = (!isActive);
    }

    void setIsVibrate(boolean state){
        isVibrate = state;
    }

    void setAlarmId(int id){
        this.alarmId = id;
    }

    void setAlarmName(String name){
        this.alarmName = name;
        Log.v(LOG_TAG, "alarm name set" + alarmName);
    }

    void setRingtonePath(String path){
        ringtonePath = path;
    }

    void setTimeInString(){
        if(min>9)
            timeInString = hour + " : " + min;
        else{
            timeInString = hour + " : 0" + min;
        }
    }

    void setAlarm(int hour, int min, CoordinatorLayout coordinatorLayout) {

        this.hour = hour;
        this.min = min;
        calendar = Calendar.getInstance();


        int MinDiff = min - calendar.get(Calendar.MINUTE);
        int HourDiff = 0;
        if (MinDiff < 0) {
            MinDiff += 60;
            HourDiff = -1;
        }
        HourDiff = HourDiff + hour - calendar.get(Calendar.HOUR_OF_DAY);
        if (HourDiff < 0) {//TODO change for next day
            HourDiff = 12 + HourDiff;
            calendar.add(Calendar.DAY_OF_MONTH, 1);

        }
        Log.v(LOG_TAG, "diff" + MinDiff);
        Log.v(LOG_TAG, "diff" + HourDiff);
        this.snackbarDialog = this.snackbarDialog + HourDiff + " Hours and " + MinDiff + " Minutes";
        Snackbar.make(coordinatorLayout, this.snackbarDialog, Snackbar.LENGTH_LONG).show();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 000);

        miliseconds = calendar.getTimeInMillis();
        Log.v(LOG_TAG, "" + miliseconds);
        //Toast.makeText(, "" + miliseconds)

    }

}
