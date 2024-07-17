// ResidentAcceptedRequestsActivity.java
package com.example.wasteapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wasteapp.person.Person;
import com.example.wasteapp.person.PersonAdapter;
import java.util.ArrayList;

public class ResidentAcceptedRequestsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private PersonAdapter personAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Person> personList;
    private int residentId = 1; // Assume this is obtained after resident login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_accepted_requests);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBHelper(this);
        displayData();
    }

    private void displayData() {
        Cursor res = dbHelper.getResidentRequests(residentId);
        if (res.getCount() == 0) {
            Toast.makeText(this, "No accepted requests available", Toast.LENGTH_SHORT).show();
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

        personAdapter = new PersonAdapter(this, personList, null, false);
        recyclerView.setAdapter(personAdapter);
    }
}
