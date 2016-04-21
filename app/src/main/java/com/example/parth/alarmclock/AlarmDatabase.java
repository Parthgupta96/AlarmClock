package com.example.parth.alarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Priyanshu on 02-04-2016.
 */
public class AlarmDatabase extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "AlarmDatabase";
    static final String TABLE_NAME = "AlarmTable";
    static  int version = 1;
    public final String LOG_TAG = AlarmDatabase.class.getSimpleName();
    static final String COLUMN_UID = "_id";
    static final String COLUMN_ISACTIVE = "is_Active";
    static final String COLUMN_ISVIBRATE = "is_Vibrate";
    static final String COLUMN_HOUR = "hour";
    static final String COLUMN_MIN = "min";
    static final String COLUMN_ALARMNAME = "alarm_name";
    static final String COLUMN_RINGTONEPATH = "ringtone_path";
    static final String COLUMN_DIFFICULTY = "difficulty_level";
    static final String COLUMN_TIMEINSTRING = "time_in_string";
    static final String COLUMN_TIMEINMILLIS = "time_in_millis";
//    static final String COLUMN_ROWID = "row_id";
    static final String createTable = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ALARMNAME + " TEXT, " + COLUMN_ISACTIVE + " VARCHAR(255), " + COLUMN_ISVIBRATE + " VARCHAR(255), " + COLUMN_HOUR + " VARCHAR(255), " + COLUMN_MIN + " VARCHAR(255), " + COLUMN_DIFFICULTY + " VARCHAR(255), " + COLUMN_RINGTONEPATH + " VARCHAR(255), " + COLUMN_TIMEINMILLIS + " VARCHAR(255));";
    static final String deleteTable = "DROPTABLE IF EXISTES " + TABLE_NAME;

    public AlarmDatabase(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    public void insertToDB(Alarm alarm){

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ISACTIVE,1);
        contentValues.put(COLUMN_ISVIBRATE,(alarm.getIsVibrate()==true?1:0));
        contentValues.put(COLUMN_HOUR, alarm.getHour());
        contentValues.put(COLUMN_MIN, alarm.getMin());
        contentValues.put(COLUMN_ALARMNAME,alarm.getAlarmName());
        contentValues.put(COLUMN_RINGTONEPATH,alarm.getRingtonePath());
        contentValues.put(COLUMN_DIFFICULTY,alarm.getDifficulty().ordinal());
        contentValues.put(COLUMN_TIMEINMILLIS, alarm.getMilliseconds());

        SQLiteDatabase db = this.getWritableDatabase();
        long change = db.insert(TABLE_NAME,null,contentValues);
        if(change<1){
            Log.v(LOG_TAG,"no change");
        }
        Log.v(LOG_TAG, "name: " + alarm.getAlarmName());
        Log.v(LOG_TAG, "id: " + alarm.getAlarmId());
        db.close();
    }

    public void updateData(Alarm alarm, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.v("in updateData ", "id: " + alarm.getAlarmId());
        contentValues.put(COLUMN_ISACTIVE, status?1:0);
        db.update(TABLE_NAME, contentValues, "_id = ?", new String[]{("" + alarm.getAlarmId())});
        Log.v(LOG_TAG, "updated");

    }
        public void updateData(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.v("in updateData ", "id: " + alarm.getAlarmId());
        contentValues.put(COLUMN_ISVIBRATE,(alarm.getIsVibrate()==true?1:0));
        contentValues.put(COLUMN_ISACTIVE,(alarm.getIsActive()==true?1:0));
        contentValues.put(COLUMN_HOUR,alarm.getHour());
        contentValues.put(COLUMN_MIN,alarm.getMin());
        contentValues.put(COLUMN_ALARMNAME,alarm.getAlarmName());
        contentValues.put(COLUMN_RINGTONEPATH, alarm.getRingtonePath());
        contentValues.put(COLUMN_DIFFICULTY, alarm.getDifficulty().ordinal());
        contentValues.put(COLUMN_TIMEINMILLIS, alarm.getMilliseconds());
        db.update(TABLE_NAME, contentValues, "_id = ?", new String[]{("" + alarm.getAlarmId())});
        Log.v(LOG_TAG, "updated");
    }

    public Cursor getCursor(){
        String columns[] ={COLUMN_UID,COLUMN_ISACTIVE,COLUMN_ISVIBRATE,COLUMN_HOUR,COLUMN_MIN,COLUMN_ALARMNAME,COLUMN_RINGTONEPATH,COLUMN_DIFFICULTY,COLUMN_TIMEINMILLIS};
        Cursor cursor = null;
        String string[]={COLUMN_ALARMNAME};
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.query(TABLE_NAME,columns,null,null,null,null,null);

        return cursor;
    }

    public Cursor sortQuery(){
        Cursor cursor;
        SQLiteDatabase  db = this.getReadableDatabase();
        String columns[] ={COLUMN_UID,COLUMN_ISACTIVE,COLUMN_ISVIBRATE,COLUMN_HOUR,COLUMN_MIN,COLUMN_ALARMNAME,COLUMN_RINGTONEPATH,COLUMN_DIFFICULTY,COLUMN_TIMEINMILLIS};
        cursor = db.query(TABLE_NAME, columns, null, null, null, null, COLUMN_TIMEINMILLIS + " ASC");
        return cursor;
    }

    public Alarm getAlarm(int id) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getReadableDatabase();
        String columns[] ={COLUMN_UID,COLUMN_ISACTIVE,COLUMN_ISVIBRATE,COLUMN_HOUR,COLUMN_MIN,COLUMN_ALARMNAME,COLUMN_RINGTONEPATH,COLUMN_DIFFICULTY};
        Cursor cursor = db.query(TABLE_NAME, columns, COLUMN_UID+"="+id, null, null, null, null);
        Alarm alarm = null;

        int isActiveIndex ;
        int isVibrateIndex ;
        int HourIndex ;
        int MinIndex ;
        int AlarmNameIndex;
        int ringtonePathIndex;
        int idTable;
        int difficultyIndex;
        String string = "";
        cursor.moveToFirst();
        if(cursor.moveToFirst()){
            alarm = new Alarm();
            idTable = cursor.getColumnIndex(AlarmDatabase.COLUMN_UID);
            isActiveIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
            isVibrateIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISVIBRATE);
            HourIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_HOUR);
            MinIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_MIN);
            AlarmNameIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ALARMNAME);
            ringtonePathIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_RINGTONEPATH);
            difficultyIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_DIFFICULTY);

            alarm.setAlarmName(cursor.getString(AlarmNameIndex));
            alarm.setIsVibrate(cursor.getInt(isVibrateIndex) == 1);
            alarm.setRingtonePath(cursor.getString(ringtonePathIndex));
            alarm.setIsActive(cursor.getInt(isActiveIndex) == 1);
            alarm.setHour(cursor.getInt(HourIndex));
            alarm.setMin(cursor.getInt(MinIndex));
            alarm.setAlarmId(cursor.getInt(idTable));
            alarm.setDifficulty(Alarm.Difficulty.values()[cursor.getInt(difficultyIndex)]);
        }
        cursor.close();
        return alarm;
    }

    public void viewData(Context context){
        Cursor cursor = getCursor();

        int isActiveIndex ;
        int isVibrateIndex ;
        int HourIndex ;
        int MinIndex ;
        int AlarmNameIndex;
        int ringtonePathIndex;
        String string = "";
        while(cursor.moveToNext()){

            isActiveIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
            isVibrateIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISVIBRATE);
            HourIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_HOUR);
            MinIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_MIN);
            AlarmNameIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ALARMNAME);
            ringtonePathIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_RINGTONEPATH);

            string =string+ cursor.getString(AlarmNameIndex)+"\t"
                    +cursor.getInt(HourIndex)+":"+cursor.getInt(MinIndex)+"\t"
                    +string+ cursor.getString(ringtonePathIndex)+"\n";
        }
        Toast.makeText(context, string, Toast.LENGTH_LONG).show();
    }

    public ArrayList<Alarm> getAllAlarms(){
        Cursor cursor = getCursor();
        ArrayList<Alarm> allAlarms = new ArrayList<>();
        int isActiveIndex ;
        int isVibrateIndex ;
        int HourIndex ;
        int MinIndex ;
        int AlarmNameIndex;
        int ringtonePathIndex;
        int id;
        int difficultyIndex;
        String string = "";
        cursor.moveToFirst();
        while(cursor.moveToNext()){
            Alarm alarm = new Alarm();
            id = cursor.getColumnIndex(AlarmDatabase.COLUMN_UID);
            isActiveIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISACTIVE);
            isVibrateIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ISVIBRATE);
            HourIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_HOUR);
            MinIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_MIN);
            AlarmNameIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_ALARMNAME);
            ringtonePathIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_RINGTONEPATH);
            difficultyIndex = cursor.getColumnIndex(AlarmDatabase.COLUMN_DIFFICULTY);

            alarm.setAlarmName(cursor.getString(AlarmNameIndex));
            alarm.setIsVibrate(cursor.getInt(isVibrateIndex) == 1);
            alarm.setRingtonePath(cursor.getString(ringtonePathIndex));
            alarm.setIsActive(cursor.getInt(isActiveIndex) == 1);
            alarm.setHour(cursor.getInt(HourIndex));
            alarm.setMin(cursor.getInt(MinIndex));
            alarm.setAlarmId(cursor.getInt(id));
            alarm.setDifficulty(Alarm.Difficulty.values()[cursor.getInt(difficultyIndex)]);
            allAlarms.add(alarm);
        }
        return  allAlarms;
    }

    public void deleteAlarm(Alarm alarm){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_UID + " = ?", new String[] {"" + alarm.getAlarmId()});
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
