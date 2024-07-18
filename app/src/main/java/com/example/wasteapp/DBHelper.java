package com.example.wasteapp;

import static com.example.wasteapp.DatabaseHelper.COL_2;
import static com.example.wasteapp.DatabaseHelper.COL_3;
import static com.example.wasteapp.DatabaseHelper.COL_4;
import static com.example.wasteapp.DatabaseHelper.COL_5;
import static com.example.wasteapp.DatabaseHelper.TABLE_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WasteApp.db";
    private static final int DATABASE_VERSION = 1;

    // Table names and columns for people
    private static final String TABLE_PEOPLE = "people_table";
    private static final String COL_PERSON_ID = "person_id";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_PHONE_NUMBER = "phone_number";
    private static final String COL_LOCATION = "location";
    private static final String COL_STATUS = "status";

    // Table names and columns for schedules
    private static final String TABLE_SCHEDULES = "schedules_table";
    private static final String COL_SCHEDULE_ID = "schedule_id";
    private static final String COL_SCHEDULE_DATE = "schedule_date";
    private static final String COL_SCHEDULE_TIME = "schedule_time";
    private static final String COL_SCHEDULE_LOCATION = "schedule_location";
    private static final String COL_GARBAGE_TYPE = "garbage_type";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create table for people
        String createPeopleTableQuery = "CREATE TABLE " + TABLE_PEOPLE + " (" +
                COL_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_FULL_NAME + " TEXT, " +
                COL_PHONE_NUMBER + " TEXT, " +
                COL_LOCATION + " TEXT, " +
                COL_STATUS + " TEXT DEFAULT 'Pending')";

        // Create table for schedules
        String createSchedulesTableQuery = "CREATE TABLE " + TABLE_SCHEDULES + " (" +
                COL_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SCHEDULE_DATE + " TEXT, " +
                COL_SCHEDULE_TIME + " TEXT, " +
                COL_SCHEDULE_LOCATION + " TEXT, " +
                COL_GARBAGE_TYPE + " TEXT)";

        db.execSQL(createPeopleTableQuery);
        db.execSQL(createSchedulesTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_PEOPLE + " ADD COLUMN " + COL_STATUS + " TEXT DEFAULT 'Pending'");
        }
    }

    public boolean insertPerson(String fullName, String phoneNumber, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULL_NAME, fullName);
        contentValues.put(COL_PHONE_NUMBER, phoneNumber);
        contentValues.put(COL_LOCATION, location);
        long result = db.insert(TABLE_PEOPLE, null, contentValues);
        return result != -1;
    }

    public Cursor getAllPendingRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PEOPLE + " WHERE " + COL_STATUS + " = 'Pending' OR " + COL_STATUS + " IS NULL", null);
    }

    public boolean updateRequestStatus(int personId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STATUS, status);
        int result = db.update(TABLE_PEOPLE, contentValues, COL_PERSON_ID + " = ?", new String[]{String.valueOf(personId)});
        return result > 0;
    }

    public boolean insertSchedule(String date, String time, String location, String garbageType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SCHEDULE_DATE, date);
        contentValues.put(COL_SCHEDULE_TIME, time);
        contentValues.put(COL_SCHEDULE_LOCATION, location);
        contentValues.put(COL_GARBAGE_TYPE, garbageType);
        long result = db.insert(TABLE_SCHEDULES, null, contentValues);
        return result != -1;
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

    public Cursor getAllSchedules() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_SCHEDULES, null);
    }
    public Cursor getResidentRequests(int residentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Assuming there's a column named 'person_id' in the schedules_table
        return db.rawQuery("SELECT * FROM " + TABLE_SCHEDULES + " WHERE " + COL_PERSON_ID + " = ?", new String[]{String.valueOf(residentId)});
    }
}
