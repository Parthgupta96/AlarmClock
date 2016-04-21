package com.example.parth.alarmclock;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmService extends Service {
    public class AlarmReceiver extends WakefulBroadcastReceiver {

        private final String LOG_TAG = AlarmReceiver.class.getSimpleName();
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.v(LOG_TAG, "in AlarmReceiver");


            Bundle bundle = intent.getExtras();
            Alarm alarm = (Alarm)bundle.getSerializable("alarm");
            if(alarm.getIsActive()) {
                Intent alarmIntend = new Intent(context, AlarmAlert.class);
                Log.v(LOG_TAG, "Going to call ringing activity");
                alarmIntend.putExtra("alarm", alarm);
                alarmIntend.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //startWakefulService(context, alarmIntend);

                context.startActivity(alarmIntend);
            }


        }
    }
    public AlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}