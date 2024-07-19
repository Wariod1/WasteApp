package com.example.wasteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "People.db";
    public static final int DATABASE_VERSION = 3;
    public static final String TABLE_NAME = "people_table";
    public static final String COL_PERSON_ID = "ID";
    public static final String COL_FULL_NAME = "FULLNAME";
    public static final String COL_PHONE_NUMBER = "PHONENUMBER";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_STATUS = "STATUS";

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
                COL_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULL_NAME + " TEXT, " +
                COL_PHONE_NUMBER + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_STATUS + " TEXT)";
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
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_STATUS + " TEXT DEFAULT 'Pending'");
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

    public boolean insertData(String name, String fullName, String phoneNumber, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULL_NAME, fullName);
        contentValues.put(COL_PHONE_NUMBER, phoneNumber);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_STATUS, "Pending");
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllPendingRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_STATUS + " = 'Pending' OR " + COL_STATUS + " IS NULL", null);
    }

    public boolean updateRequestStatus(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STATUS, status);
        int result = db.update(TABLE_NAME, contentValues, COL_PERSON_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public Cursor getResidentRequests(int residentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_PERSON_ID + " = ? AND " + COL_STATUS + " = 'Accepted'", new String[]{String.valueOf(residentId)});
    }

    public boolean insertSchedule(String selectedDate, String date, String time, String location) {
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
