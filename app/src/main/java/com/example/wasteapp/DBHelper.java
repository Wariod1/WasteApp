// DBHelper.java
package com.example.wasteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "People.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "people_table";
    private static final String COL_1 = "ID";
    private static final String COL_2 = "FULLNAME";
    private static final String COL_3 = "PHONENUMBER";
    private static final String COL_4 = "LOCATION";
    private static final String COL_5 = "STATUS";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_2 + " TEXT, "
                + COL_3 + " TEXT, "
                + COL_4 + " TEXT, "
                + COL_5 + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_5 + " TEXT DEFAULT 'Pending'");
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

    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
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
}
