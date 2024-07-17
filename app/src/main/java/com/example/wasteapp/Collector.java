package com.example.wasteapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wasteapp.person.Person;
import com.example.wasteapp.person.PersonAdapter;
import java.util.ArrayList;

public class Collector extends AppCompatActivity implements PersonAdapter.OnItemClickListener {
    private static final String TAG = "Collector";
    private DBHelper dbHelper;
    private PersonAdapter personAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Person> personList;
    private String collectorEmail = "collector@example.com"; // This should be dynamically set based on the logged-in collector

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collector);

        try {
            recyclerView = findViewById(R.id.recyclerView);
            if (recyclerView == null) {
                throw new NullPointerException("RecyclerView not found in layout");
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            dbHelper = new DBHelper(this);
            displayData();

            Button btnAddSchedule = findViewById(R.id.btnAddSchedule);
            btnAddSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Collector.this, AddScheduleActivity.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void displayData() {
        try {
            Cursor res = dbHelper.getAllPendingRequests();
            if (res == null) {
                throw new NullPointerException("Cursor is null");
            }
            if (res.getCount() == 0) {
                Toast.makeText(this, "No requests available", Toast.LENGTH_SHORT).show();
                return;
            }
            personList = new ArrayList<>();
            while (res.moveToNext()) {
                personList.add(new Person(
                        res.getString(0),
                        res.getString(1),
                        res.getString(2),
                        res.getString(3)
                ));
            }
            personAdapter = new PersonAdapter(this, personList, this, true);
            recyclerView.setAdapter(personAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error in displayData: ", e);
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAcceptClick(Person person) {
        try {
            acceptRequest(person);
        } catch (Exception e) {
            Log.e(TAG, "Error in onAcceptClick: ", e);
            Toast.makeText(this, "Error accepting request: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void acceptRequest(Person person) {
        boolean isUpdated = dbHelper.updateRequestStatus(person.getId(), "Accepted");
        if (isUpdated) {
            Toast.makeText(this, "Request accepted", Toast.LENGTH_SHORT).show();
            personList.remove(person);
            personAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Failed to accept request", Toast.LENGTH_SHORT).show();
        }
    }
}