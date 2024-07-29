package com.example.wasteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ScheduleDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "schedule.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "schedules";
    private static final String COL_ID = "id";
    private static final String COL_DATE = "date";
    private static final String COL_TIME = "time";
    private static final String COL_LOCATION = "location";
    private static final String COL_WASTE_TYPE = "waste_type";

    public ScheduleDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_DATE + " TEXT, " +
                COL_TIME + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_WASTE_TYPE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertSchedule(String date, String time, String location, String wasteType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_WASTE_TYPE, wasteType);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllSchedules() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Sort by date in descending order
        return db.rawQuery("SELECT * FROM schedules ORDER BY date DESC", null);
    }


}
