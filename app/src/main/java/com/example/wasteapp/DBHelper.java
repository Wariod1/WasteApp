package com.example.wasteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "People.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "people_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "FULLNAME";
    private static final String COL_3 = "PHONENUMBER";
    private static final String COL_4 = "LOCATION";
    private static final String COL_5 = "STATUS";

    // New table for schedules
    private static final String TABLE_SCHEDULE = "schedule_table";
    private static final String SCHEDULE_ID = "ID";
    private static final String SCHEDULE_DATE = "DATE";
    private static final String SCHEDULE_TIME = "TIME";
    private static final String SCHEDULE_LOCATION = "LOCATION";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " TEXT, " +
                COL_5 + " TEXT)";
        db.execSQL(createTableQuery);

        String createScheduleTable = "CREATE TABLE " + TABLE_SCHEDULE + " (" +
                SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SCHEDULE_DATE + " TEXT, " +
                SCHEDULE_TIME + " TEXT, " +
                SCHEDULE_LOCATION + " TEXT)";
        db.execSQL(createScheduleTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_5 + " TEXT DEFAULT 'Pending'");
        }
        if (oldVersion < 3) {
            String createScheduleTable = "CREATE TABLE " + TABLE_SCHEDULE + " (" +
                    SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SCHEDULE_DATE + " TEXT, " +
                    SCHEDULE_TIME + " TEXT, " +
                    SCHEDULE_LOCATION + " TEXT)";
            db.execSQL(createScheduleTable);
        }
    }

    public boolean insertData(String fullName, String phoneNumber, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, fullName);
        contentValues.put(COL_3, phoneNumber);
        contentValues.put(COL_4, location);
        contentValues.put(COL_5, "Pending");
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllPendingRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_5 + " = 'Pending' OR " + COL_5 + " IS NULL", null);
    }

    public boolean updateRequestStatus(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_5, status);
        int result = db.update(TABLE_NAME, contentValues, COL_1 + " = ?", new String[]{id});
        return result > 0;
    }

    public Cursor getResidentRequests(int residentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = ? AND " + COL_5 + " = 'Accepted'", new String[]{String.valueOf(residentId)});
    }

    public boolean insertSchedule(String date, String time, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SCHEDULE_DATE, date);
        contentValues.put(SCHEDULE_TIME, time);
        contentValues.put(SCHEDULE_LOCATION, location);
        long result = db.insert(TABLE_SCHEDULE, null, contentValues);
        return result != -1;
    }

    public Cursor getAllSchedules() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SCHEDULE, null);
    }
}