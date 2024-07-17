package com.example.wasteapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddScheduleActivity extends AppCompatActivity {
    private CalendarView calendarView;
    private EditText timeEditText, locationEditText;
    private Button saveButton;
    private DBHelper dbHelper;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        dbHelper = new DBHelper(this);

        calendarView = findViewById(R.id.calendarView);
        timeEditText = findViewById(R.id.timeEditText);
        locationEditText = findViewById(R.id.locationEditText);
        saveButton = findViewById(R.id.saveButton);

        // Set initial date
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(calendar.getTime());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                selectedDate = dateFormat.format(calendar.getTime());
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String time = timeEditText.getText().toString();
                String location = locationEditText.getText().toString();

                if (selectedDate.isEmpty() || time.isEmpty() || location.isEmpty()) {
                    Toast.makeText(AddScheduleActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.insertSchedule(selectedDate, time, location)) {
                    Toast.makeText(AddScheduleActivity.this, "Schedule added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddScheduleActivity.this, "Failed to add schedule", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}