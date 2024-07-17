package com.example.wasteapp;

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
                Schedule schedule = new Schedule(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                scheduleList.add(schedule);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}