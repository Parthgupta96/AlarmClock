package com.example.parth.alarmclock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

/**
 * Created by Parth on 28-03-2016.
 */
public class AlarmReceiver extends WakefulBroadcastReceiver {

    private final String LOG_TAG = AlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "in AlarmReceiver");


        Bundle bundle = new Bundle();
        Intent alarmIntend = new Intent(context, AlarmAlert.class);
        Log.v(LOG_TAG, "Going to call ringing activity");
        alarmIntend.putExtras(bundle);
        alarmIntend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startService(alarmIntend);
        //startWakefulService(context, alarmIntend);
        context.startActivity(alarmIntend);
        //alarmCallFunction(alarmIntend);

    }
}
