package com.example.parth.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Parth on 28-03-2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    private final String LOG_TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v(LOG_TAG, "in AlarmReceiver");
        Bundle bundle = intent.getBundleExtra("bundle");
        Alarm alarm = (Alarm) bundle.get("alarm");
        if (alarm != null && alarm.getIsActive()) {
            Intent alarmIntend = new Intent(context, AlarmAlertActivity.class);
            Log.v(LOG_TAG, "Going to call ringing activity");
            alarmIntend.putExtra("alarm", alarm);
            alarmIntend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startWakefulService(context, alarmIntend);
            context.startActivity(alarmIntend);
//            context.startService(alarmIntend);
        }


    }
}
