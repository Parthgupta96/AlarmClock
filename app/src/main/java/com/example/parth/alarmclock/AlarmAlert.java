package com.example.parth.alarmclock;

import android.app.ActionBar;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;

public class AlarmAlert extends AppCompatActivity {

    public MediaPlayer mediaPlayer;
    Vibrator vibrator;
    Alarm alarm;
    TextView answer;
    TextView question;
    String questionString;
    String actualAnswer;
    private final String LOG_TAG = AlarmAlert.class.getSimpleName();
    String answerString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "in AlarmAlert");
        unlockScreen();
        setContentView(R.layout.activity_alarm_alert);

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");

        //Uri uri = alarm.getRingtonePath();
        question = (TextView)findViewById(R.id.question);
        answer = (TextView) findViewById(R.id.answer);

        String diff = alarm.getDifficulty().toString();
        questionString = GenerateMathsQuestion.generateQuestion(diff);
        question.setText(questionString);
        actualAnswer = EvaluateString.evaluate(questionString);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setVolume(1.0f, 1.0f);
        mediaPlayer.setLooping(true);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);

        try {
            mediaPlayer.setDataSource(this,Uri.parse(alarm.getRingtonePath()));
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();

        if(alarm.getIsVibrate()) {
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = {1000, 200, 200, 200};
            vibrator.vibrate(pattern, 0);
        }
    }

    public void button1(View view){
        answerString = answerString + "1";
        answer.setText(answerString);
    }

    public void button2(View view){
        answerString = answerString + "2";
        answer.setText(answerString);
    }

    public void button3(View view){
        answerString = answerString + "3";
        answer.setText(answerString);
    }

    public void button4(View view){
        answerString = answerString + "4";
        answer.setText(answerString);
    }

    public void button5(View view){
        answerString = answerString + "5";
        answer.setText(answerString);
    }

    public void button6(View view){
        answerString = answerString + "6";
        answer.setText(answerString);
    }

    public void button7(View view){
        answerString = answerString + "7";
        answer.setText(answerString);
    }

    public void button8(View view){
        answerString = answerString + "8";
        answer.setText(answerString);
    }

    public void button9(View view){
        answerString = answerString + "9";
        answer.setText(answerString);
    }

    public void button0(View view){
        answerString = answerString + "0";
        answer.setText(answerString);
    }

    public void submit(View view){
        if(answerString.equals(actualAnswer)){
            closeAlarm();
        }
    }

    public void clear(View view){
        if(answerString.equals("")){}
        else {
            answerString = answerString.substring(0, answerString.length() - 1);
            answer.setText(answerString);
        }
    }

    protected void unlockScreen(){
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    public void closeAlarm(){
        Log.v(LOG_TAG, "will now stop");
        mediaPlayer.stop();
        if(vibrator!=null)
            vibrator.cancel();
        Log.v(LOG_TAG, "will now release");
        mediaPlayer.release();
        Log.v(LOG_TAG, "id of ringing alarm: " + alarm.getAlarmId());
        alarm.setIsActive(false);
        AlarmDatabase alarmDatabase = new AlarmDatabase(this);
        alarmDatabase.updateData(alarm);
//        AlarmDatabase alarmDatabase = new AlarmDatabase(this);
//        Cursor cursor = alarmDatabase.sortQuery();
//        while(cursor.moveToNext()){
//            if((alarm.getAlarmId().equals("" + cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COLUMN_UID))))){
//                break;
//            }
//            cursor.moveToNext();
//            Alarm newAlarm = alarmDatabase.getAlarm(cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COLUMN_UID)));
//            newAlarm.scheduleAlarm(this);
//        }

        this.finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
        return super.onCreateOptionsMenu(menu);

    }
}
