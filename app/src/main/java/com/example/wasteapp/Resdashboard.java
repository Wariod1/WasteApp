package com.example.wasteapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Resdashboard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resdashboard);

        Button pickup = findViewById(R.id.btnPick);
        pickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Resdashboard.this, ResidentActivity.class);
                startActivity(intent);
            }
        });

        Button acceptedRequests = findViewById(R.id.btnAcceptedRequests);
        acceptedRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Resdashboard.this, ResidentAcceptedRequestsActivity.class);
                startActivity(intent);
            }
        });

        Button viewSchedule = findViewById(R.id.btnSchedule);
        viewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Resdashboard.this, ViewScheduleActivity.class);
                startActivity(intent);
            }
        });
    }
}