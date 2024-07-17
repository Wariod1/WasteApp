package com.example.wasteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResidentActivity extends AppCompatActivity {

    private EditText editTextFullName, editTextPhoneNumber, editTextLocation;
    private Button buttonSubmit;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident);

        editTextFullName = findViewById(R.id.editTextFullName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextLocation = findViewById(R.id.editTextLocation);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        dbHelper = new DBHelper(this);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editTextFullName.getText().toString().trim();
                String phoneNumber = editTextPhoneNumber.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();

                if (fullName.isEmpty() || phoneNumber.isEmpty() || location.isEmpty()) {
                    Toast.makeText(ResidentActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.insertData(fullName, phoneNumber, location);
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
