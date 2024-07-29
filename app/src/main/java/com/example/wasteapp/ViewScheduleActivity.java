package com.example.wasteapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ViewScheduleActivity extends AppCompatActivity {
    private static final String TAG = "ViewScheduleActivity";
    private ScheduleDBHelper scheduleDBHelper;
    private ScheduleAdapter scheduleAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Schedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        scheduleDBHelper = new ScheduleDBHelper(this);
        displaySchedules();
    }

    private void displaySchedules() {
        try {
            Cursor res = scheduleDBHelper.getAllSchedules();
            if (res == null) {
                throw new NullPointerException("Cursor is null");
            }
            Log.d(TAG, "Number of schedules: " + res.getCount());
            if (res.getCount() == 0) {
                Toast.makeText(this, "No schedules available", Toast.LENGTH_SHORT).show();
                return;
            }

            scheduleList = new ArrayList<>();
            while (res.moveToNext()) {
                Log.d(TAG, "Date: " + res.getString(1) + ", Time: " + res.getString(2) +
                        ", Location: " + res.getString(3) + ", Waste Type: " + res.getString(4));
                scheduleList.add(new Schedule(
                        res.getString(1),
                        res.getString(2),
                        res.getString(3),
                        res.getString(4)
                ));
            }
            res.close(); // Make sure to close the cursor after use

            scheduleAdapter = new ScheduleAdapter(this, scheduleList);
            recyclerView.setAdapter(scheduleAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error in displaySchedules: ", e);
            Toast.makeText(this, "Error loading schedules: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
