package com.example.wasteapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class ResDashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resdashboard);

        Button pickup = findViewById(R.id.btnPick);
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResDashboard.this, ResidentActivity.class);
                startActivity(intent);
            }
        });

        Button viewScheduleButton = findViewById(R.id.btnViewSchedule);
        viewScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResDashboard.this, ViewScheduleActivity.class);
                startActivity(intent);
            }
        });

        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResDashboard.this, Login.class); // Assuming LoginActivity is your login screen
                startActivity(intent);
                finish(); // Finish current activity to prevent returning to it on back press
            }
        });
        Button viewAcceptedRequestsButton = findViewById(R.id.btnViewAcceptedRequests);
        viewAcceptedRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResDashboard.this, AcceptedRequestsActivity.class));
            }
        });

        Button btnViewPrice = findViewById(R.id.btnViewPrice);
        btnViewPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResDashboard.this, MainActivity.class));
            }
        });

    }
}
