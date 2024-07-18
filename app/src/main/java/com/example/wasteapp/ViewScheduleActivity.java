package com.example.wasteapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ViewScheduleActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScheduleAdapter adapter;
    private List<Schedule> scheduleList;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        dbHelper = new DBHelper(this);
        recyclerView = findViewById(R.id.scheduleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleList = new ArrayList<>();
        loadSchedules();

        adapter = new ScheduleAdapter(this, scheduleList);
        recyclerView.setAdapter(adapter);
    }

    private void loadSchedules() {
        Cursor cursor = dbHelper.getAllSchedules();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") Schedule schedule = new Schedule(
                        cursor.getString(cursor.getColumnIndex("schedule_id")),
                        cursor.getString(cursor.getColumnIndex("schedule_date")),
                        cursor.getString(cursor.getColumnIndex("schedule_time")),
                        cursor.getString(cursor.getColumnIndex("schedule_location")),
                        cursor.getString(cursor.getColumnIndex("garbage_type"))
                );
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
