package com.example.wasteapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddScheduleActivity extends AppCompatActivity {
    private ScheduleDBHelper scheduleDBHelper;
    private EditText etDate, etTime;
    private Spinner spLocation, spWasteType;
    private Button btnAddSchedule;
    private Calendar selectedDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        scheduleDBHelper = new ScheduleDBHelper(this);
        etDate = findViewById(R.id.etDate);
        etTime = findViewById(R.id.etTime);
        spLocation = findViewById(R.id.spLocation);
        spWasteType = findViewById(R.id.spWasteType);
        btnAddSchedule = findViewById(R.id.btnAddSchedule);
        selectedDateTime = Calendar.getInstance();

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = etDate.getText().toString();
                String time = etTime.getText().toString();
                String location = spLocation.getSelectedItem().toString();
                String wasteType = spWasteType.getSelectedItem().toString();

                if (date.isEmpty() || time.isEmpty() || location.isEmpty() || wasteType.isEmpty()) {
                    Toast.makeText(AddScheduleActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (isPastDateTime()) {
                    Toast.makeText(AddScheduleActivity.this, "Cannot select a past date and time", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = scheduleDBHelper.insertSchedule(date, time, location, wasteType);
                    if (isInserted) {
                        Toast.makeText(AddScheduleActivity.this, "Schedule added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddScheduleActivity.this, "Failed to add schedule", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, monthOfYear);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis()); // Set minimum date to today
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        etTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private boolean isPastDateTime() {
        Calendar currentDateTime = Calendar.getInstance();
        return selectedDateTime.before(currentDateTime);
    }
}
