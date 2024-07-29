package com.example.wasteapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "People.db";
    public static final int DATABASE_VERSION = 9; // Increment the database version
    public static final String TABLE_NAME = "people_table";
    public static final String COL_PERSON_ID = "ID";
    public static final String COL_FULL_NAME = "FULLNAME";
    public static final String COL_PHONE_NUMBER = "PHONENUMBER";
    public static final String COL_LOCATION = "LOCATION";
    public static final String COL_STATUS = "STATUS";
    public static final String COL_ROLE = "ROLE";
    public static final String COL_WASTE_TYPE = "WASTE_TYPE";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COL_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_FULL_NAME + " TEXT, "
                + COL_PHONE_NUMBER + " TEXT, "
                + COL_LOCATION + " TEXT, "
                + COL_STATUS + " TEXT, "
                + COL_ROLE + " TEXT DEFAULT 'User', "
                + COL_WASTE_TYPE + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_ROLE + " TEXT DEFAULT 'User'");
        }
        if (oldVersion < 6) {
            if (!columnExists(db, TABLE_NAME, COL_WASTE_TYPE)) {
                db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COL_WASTE_TYPE + " TEXT");
            }
        }
    }

    private boolean columnExists(SQLiteDatabase db, String tableName, String columnName) {
        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);
        int nameIndex = cursor.getColumnIndex("name");
        while (cursor.moveToNext()) {
            if (cursor.getString(nameIndex).equals(columnName)) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public Cursor getAllPendingRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, new String[]{
                COL_PERSON_ID,
                COL_FULL_NAME,
                COL_PHONE_NUMBER,
                COL_LOCATION,
                COL_WASTE_TYPE
        }, COL_STATUS + " = ? OR " + COL_STATUS + " IS NULL", new String[]{"Pending"}, null, null, null);
    }

    public boolean updateRequestStatus(String id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STATUS, status);
        int result = db.update(TABLE_NAME, contentValues, COL_PERSON_ID + " = ?", new String[]{id});
        return result > 0;
    }

    public Cursor getAllAcceptedRequests() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_STATUS + "='Accepted' ORDER BY " + COL_PERSON_ID + " DESC", null);
    }
    @SuppressLint("Range")
    public ArrayList<String> getAllResidentTokens() {
        ArrayList<String> tokens = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT token FROM residents", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                tokens.add(cursor.getString(cursor.getColumnIndex("token")));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return tokens;
    }


    public boolean insertRequest(String fullName, String phoneNumber, String location, String wasteType) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FULL_NAME, fullName);
        contentValues.put(COL_PHONE_NUMBER, phoneNumber);
        contentValues.put(COL_LOCATION, location);
        contentValues.put(COL_STATUS, "Pending");
        contentValues.put(COL_ROLE, "User");
        contentValues.put(COL_WASTE_TYPE, wasteType);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }
}