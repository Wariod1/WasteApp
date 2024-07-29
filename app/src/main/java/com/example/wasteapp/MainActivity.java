package com.example.wasteapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DBPrice dbPrice;
    private EditText editTextQuantity;
    private TextView textViewPrice;
    private Button buttonGetPrice;
    private Button buttonPay;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DBHelper
        dbPrice = new DBPrice(this);

        // Initialize UI elements
        editTextQuantity = findViewById(R.id.editTextQuantity);
        textViewPrice = findViewById(R.id.textViewPrice);
        buttonGetPrice = findViewById(R.id.buttonGetPrice);
        buttonPay = findViewById(R.id.buttonPay);

        // Set up button click listener
        buttonGetPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get quantity from EditText
                String quantityString = editTextQuantity.getText().toString().trim();
                if (!quantityString.isEmpty()) {
                    int quantity = Integer.parseInt(quantityString);

                    // Get price for the quantity
                    double price = dbPrice.getPriceForQuantity(quantity);

                    // Display the price
                    if (price != -1) {
                        textViewPrice.setText("Price: Ksh " + price);
                    } else {
                        textViewPrice.setText("No price available for this quantity.");
                    }
                }
            }
        });


    }
}
