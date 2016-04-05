package com.example.parth.alarmclock;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Priyanshu on 02-04-2016.
 */
public class AlarmDatabase extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "AlarmDatabase";
    static final String TABLE_NAME = "AlarmTable";
    static final  int version = 1;

    static final String COLUMN_UID = "_id";
    static final String COLUMN_ISACTIVE = "is Active";
    static final String COLUMN_ISVIBRATE = "is Vibrate";
    static final String COLUMN_HOUR = "hour";
    static final String COLUMN_MIN = "min";
    static final String COLUMN_ALARMNAME = "alarm name";
    static final String COLUMN_RINGTONEPATH = "ringtone path";
    static final String COLUMN_TIMEINSTRING = "time in string";
    static final String createTable = "CREATE TABLE " + TABLE_NAME + "(" + COLUMN_UID + "INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_ALARMNAME + "VARCHAR(255)" + COLUMN_ISACTIVE + "VARCHAR(255)" + COLUMN_ISVIBRATE + "VARCHAR(255)" + COLUMN_HOUR + "VARCHAR(255)" + COLUMN_MIN + "VARCHAR(255)" + COLUMN_RINGTONEPATH + "VARCHAR(255)" + COLUMN_TIMEINSTRING + "VARCHAR(255));";
    static final String deleteTable = "DROPTABLE IF EXISTES " + TABLE_NAME;


    public AlarmDatabase(Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(deleteTable);
        onCreate(sqLiteDatabase);
    }
}
