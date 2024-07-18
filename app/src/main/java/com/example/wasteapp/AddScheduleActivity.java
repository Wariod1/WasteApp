package com.example.wasteapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddScheduleActivity extends AppCompatActivity {
    private TimePicker timePicker;
    private Spinner locationSpinner, garbageTypeSpinner;
    private Button saveButton, dateButton;
    private DBHelper dbHelper;
    private String selectedDate;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        dbHelper = new DBHelper(this);

        timePicker = findViewById(R.id.timePicker);
        locationSpinner = findViewById(R.id.locationSpinner);
        garbageTypeSpinner = findViewById(R.id.garbageTypeSpinner);
        saveButton = findViewById(R.id.saveButton);
        dateButton = findViewById(R.id.dateButton);

        calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = dateFormat.format(calendar.getTime());

        dateButton.setText(selectedDate);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                selectedDate = dateFormat.format(calendar.getTime());
                                dateButton.setText(selectedDate);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Disallow past dates
                datePickerDialog.show();
            }
        });

        // Populate location Spinner
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(
                this, R.array.locations_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(locationAdapter);

        // Populate garbage type Spinner
        ArrayAdapter<CharSequence> garbageTypeAdapter = ArrayAdapter.createFromResource(
                this, R.array.select_garbage_type, android.R.layout.simple_spinner_item);
        garbageTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        garbageTypeSpinner.setAdapter(garbageTypeAdapter);

        // Remove the analog clock from TimePicker
        try {
            View timePickerView = timePicker.getChildAt(0);
            if (timePickerView instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) timePickerView;
                View firstChild = viewGroup.getChildAt(0);
                if (firstChild instanceof ViewGroup) {
                    ViewGroup viewGroupChild = (ViewGroup) firstChild;
                    View secondChild = viewGroupChild.getChildAt(0);
                    if (secondChild instanceof EditText) {
                        secondChild.setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                String time = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);

                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    Toast.makeText(AddScheduleActivity.this, "Please select a future date and time", Toast.LENGTH_SHORT).show();
                    return;
                }

                String location = locationSpinner.getSelectedItem().toString();
                String garbageType = garbageTypeSpinner.getSelectedItem().toString();

                if (location.isEmpty() || garbageType.isEmpty()) {
                    Toast.makeText(AddScheduleActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHelper.insertSchedule(selectedDate, time, location, garbageType)) {
                    Toast.makeText(AddScheduleActivity.this, "Schedule added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddScheduleActivity.this, "Failed to add schedule", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
