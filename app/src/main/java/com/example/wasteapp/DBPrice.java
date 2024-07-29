package com.example.wasteapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBPrice extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "WasteApp.db";
    public static final int DATABASE_VERSION = 8; // Increment the database version for new tables

    // Table for price ranges
    public static final String TABLE_PRICE = "price_table";
    public static final String COL_MIN_QUANTITY = "MIN_QUANTITY";
    public static final String COL_MAX_QUANTITY = "MAX_QUANTITY";
    public static final String COL_PRICE = "PRICE";

    public DBPrice(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the price table
        String createPriceTableQuery = "CREATE TABLE " + TABLE_PRICE + " ("
                + COL_MIN_QUANTITY + " INTEGER, "
                + COL_MAX_QUANTITY + " INTEGER, "
                + COL_PRICE + " REAL, "
                + "PRIMARY KEY (" + COL_MIN_QUANTITY + ", " + COL_MAX_QUANTITY + "))";
        db.execSQL(createPriceTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Upgrade the price table if necessary
        if (oldVersion < 8) {
            db.execSQL("CREATE TABLE " + TABLE_PRICE + " ("
                    + COL_MIN_QUANTITY + " INTEGER, "
                    + COL_MAX_QUANTITY + " INTEGER, "
                    + COL_PRICE + " REAL, "
                    + "PRIMARY KEY (" + COL_MIN_QUANTITY + ", " + COL_MAX_QUANTITY + "))");
        }
    }

    public boolean insertPriceRange(int minQuantity, int maxQuantity, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MIN_QUANTITY, minQuantity);
        contentValues.put(COL_MAX_QUANTITY, maxQuantity);
        contentValues.put(COL_PRICE, price);
        long result = db.insert(TABLE_PRICE, null, contentValues);
        return result != -1;
    }

    @SuppressLint("Range")
    public double getPriceForQuantity(int quantity) {
        SQLiteDatabase db = this.getReadableDatabase();
        double price = -1; // Default to -1 to indicate not found

        String query = "SELECT " + COL_PRICE + " FROM " + TABLE_PRICE +
                " WHERE " + COL_MIN_QUANTITY + " <= ? AND " + COL_MAX_QUANTITY + " >= ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(quantity), String.valueOf(quantity)});

        if (cursor.moveToFirst()) {
            price = cursor.getDouble(cursor.getColumnIndex(COL_PRICE));
        }
        cursor.close();
        return price;
    }
}
