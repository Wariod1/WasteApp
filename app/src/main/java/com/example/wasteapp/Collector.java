package com.example.wasteapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Collector extends AppCompatActivity {

    private TextView textViewDisplay;
    private DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident2);

        textViewDisplay = findViewById(R.id.textViewDisplay);
        dbHelper = new DBHelper(this);

        displayData();
    }

    @SuppressLint("SetTextI18n")
    private void displayData() {
        Cursor res = dbHelper.getAllData();
        if (res.getCount() == 0) {
            textViewDisplay.setText("No data found");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        while (res.moveToNext()) {
            stringBuilder.append("ID: ").append(res.getString(0)).append("\n");
            stringBuilder.append("Full Name: ").append(res.getString(1)).append("\n");
            stringBuilder.append("Phone Number: ").append(res.getString(2)).append("\n");
            stringBuilder.append("Location: ").append(res.getString(3)).append("\n\n");
        }
        textViewDisplay.setText(stringBuilder.toString());
    }
}