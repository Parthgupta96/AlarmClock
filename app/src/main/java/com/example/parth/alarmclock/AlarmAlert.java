package com.example.parth.alarmclock;

import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.database.Cursor;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.IOException;

public class AlarmAlert extends AppCompatActivity {

    public MediaPlayer mediaPlayer;
    Vibrator vibrator;
    Vibrator wrongAnsVibrator;
    Alarm alarm, currentAlarm;
    TextView answer;
    TextView question;
    String questionString;
    String actualAnswer;
    private final String LOG_TAG = AlarmAlert.class.getSimpleName();
    String answerString = "";
    ColorStateList oldColors;
    Cursor cursor;
    AlarmDatabase alarmDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "in AlarmAlert");
        unlockScreen();
        setContentView(R.layout.activity_alarm_alert);

        Bundle bundle = this.getIntent().getExtras();
        alarm = (Alarm) bundle.getSerializable("alarm");
        alarmDatabase = new AlarmDatabase(this);

        //Uri uri = alarm.getRingtonePath();
        question = (TextView)findViewById(R.id.question);
        answer = (TextView) findViewById(R.id.answer);

        oldColors =  answer.getTextColors();
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
        answer.setTextColor(oldColors);
    }

    public void button2(View view){
        answerString = answerString + "2";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button3(View view){
        answerString = answerString + "3";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button4(View view){
        answerString = answerString + "4";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button5(View view){
        answerString = answerString + "5";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button6(View view){
        answerString = answerString + "6";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button7(View view){
        answerString = answerString + "7";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button8(View view){
        answerString = answerString + "8";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button9(View view){
        answerString = answerString + "9";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void button0(View view){
        answerString = answerString + "0";
        answer.setText(answerString);
        answer.setTextColor(oldColors);
    }

    public void submit(View view){
        if(answerString.equals(actualAnswer)){
            closeAlarm();
        }else{
            wrongAnsVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            wrongAnsVibrator.vibrate(500);
            answer.setTextColor(getResources().getColor(R.color.wrongAnswer));
            Animation shake = AnimationUtils.loadAnimation(AlarmAlert.this, R.anim.shake);
            answer.startAnimation(shake);

        }
    }

    public void clear(View view){
        if(answerString.equals("")){
        }
        else {
            answerString = answerString.substring(0, answerString.length() - 1);
            answer.setText(answerString);
            if(answerString.equals("")){
                answer.setText("Answer");
            }else{
            answer.setTextColor(oldColors);
            }
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
        alarmDatabase.updateData(alarm);
        cursor = alarmDatabase.sortQuery();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(AlarmDatabase.COLUMN_UID));
            currentAlarm = alarmDatabase.getAlarm(id);
            Log.v(LOG_TAG, "id of next alarm " + id);
            if(currentAlarm != null) {
                if (currentAlarm.getIsActive() == true) {
                    currentAlarm.scheduleAlarm(this, true);
                    break;
                }
            }
        }
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
