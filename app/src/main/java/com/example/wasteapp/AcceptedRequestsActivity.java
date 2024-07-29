package com.example.wasteapp;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.wasteapp.person.Person;
import com.example.wasteapp.person.PersonAdapter;
import java.util.ArrayList;

public class AcceptedRequestsActivity extends AppCompatActivity {
    private static final String TAG = "AcceptedRequests";
    private DBHelper dbHelper;
    private PersonAdapter personAdapter;
    private RecyclerView recyclerView;
    private ArrayList<Person> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_requests);

        recyclerView = findViewById(R.id.recyclerViewAccepted);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DBHelper(this);
        displayAcceptedRequests();
    }

    private void displayAcceptedRequests() {
        try {
            Cursor res = dbHelper.getAllAcceptedRequests();
            if (res == null) {
                throw new NullPointerException("Cursor is null");
            }
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
                        res.getString(3),
                        res.getString(4) // Add waste type
                ));
            }

            personAdapter = new PersonAdapter(this, personList, null, false);
            recyclerView.setAdapter(personAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error in displayAcceptedRequests: ", e);
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
