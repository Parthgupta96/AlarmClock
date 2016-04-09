package com.example.parth.alarmclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

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
    static final String COLUMN_TIMEINSTRING = "time_in_string";
    static final String createTable = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ALARMNAME + " TEXT, " + COLUMN_ISACTIVE + " VARCHAR(255), " + COLUMN_ISVIBRATE + " VARCHAR(255), " + COLUMN_HOUR + " VARCHAR(255), " + COLUMN_MIN + " VARCHAR(255), " + COLUMN_RINGTONEPATH + " VARCHAR(255));";
    static final String deleteTable = "DROPTABLE IF EXISTES " + TABLE_NAME;


    public AlarmDatabase(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    public void insertToDB(Alarm alarm){

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ISACTIVE,1);
        contentValues.put(COLUMN_ISVIBRATE,(alarm.getIsVibrate()==true?1:0));
        contentValues.put(COLUMN_HOUR,alarm.getHour());
        contentValues.put(COLUMN_MIN,alarm.getMin());
        contentValues.put(COLUMN_ALARMNAME,alarm.getAlarmName());
        contentValues.put(COLUMN_RINGTONEPATH,alarm.getRingtonePath());

        SQLiteDatabase db = this.getWritableDatabase();
       long change = db.insert(TABLE_NAME,null,contentValues);
        if(change<1){
            Log.v(LOG_TAG,"no change");
        }
        Log.v(LOG_TAG,""+alarm.getAlarmName());
        db.close();
    }
    public Cursor getCursor(){
        String columns[] ={COLUMN_ISACTIVE,COLUMN_ISVIBRATE,COLUMN_HOUR,COLUMN_MIN,COLUMN_ALARMNAME,COLUMN_RINGTONEPATH};
        Cursor cursor = null;
        String string[]={COLUMN_ALARMNAME};
        SQLiteDatabase db = this.getReadableDatabase();
        cursor = db.query(TABLE_NAME,columns,null,null,null,null,null);

        return cursor;
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
