package com.example.wasteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView; // Add this import
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResidentActivity extends AppCompatActivity {
    private EditText editTextFullName, editTextPhoneNumber;
    private Spinner spinnerLocation, spinnerWasteType;
    private Button buttonSubmit;
    private DBHelper dbHelper;
    private TextView textViewSelectLocation; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        spinnerLocation = findViewById(R.id.spinnerLocation);
        spinnerWasteType = findViewById(R.id.spinnerWasteType);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        textViewSelectLocation = findViewById(R.id.textViewSelectLocation); // Add this line

        dbHelper = new DBHelper(this);

        // Ensure the TextView is visible
        if (textViewSelectLocation != null) {
            textViewSelectLocation.setVisibility(View.VISIBLE);
            textViewSelectLocation.setText("Select Location");
        }

        // Set up the Location Spinner
        ArrayAdapter<CharSequence> locationAdapter = ArrayAdapter.createFromResource(this,
                R.array.locations_array, android.R.layout.simple_spinner_item);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocation.setAdapter(locationAdapter);

        // Set up the Waste Type Spinner
        ArrayAdapter<CharSequence> wasteTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.waste_types_array, android.R.layout.simple_spinner_item);
        wasteTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWasteType.setAdapter(wasteTypeAdapter);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String location = spinnerLocation.getSelectedItem().toString();
                String wasteType = spinnerWasteType.getSelectedItem().toString();

                if (fullName.isEmpty() || phoneNumber.isEmpty() || location.isEmpty() || wasteType.isEmpty()) {
                    Toast.makeText(ResidentActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.insertData(fullName, phoneNumber, location, wasteType);
                    if (isInserted) {
                        Toast.makeText(ResidentActivity.this, "Request submitted successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ResidentActivity.this, Resdashboard.class));
                        finish();
                    } else {
                        Toast.makeText(ResidentActivity.this, "Request not submitted. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}