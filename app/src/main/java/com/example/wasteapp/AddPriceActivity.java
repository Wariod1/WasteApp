package com.example.wasteapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddPriceActivity extends AppCompatActivity {
    private EditText editTextMinQuantity, editTextMaxQuantity, editTextPrice;
    private DBPrice dbPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_price);

        editTextMinQuantity = findViewById(R.id.editTextMinQuantity);
        editTextMaxQuantity = findViewById(R.id.editTextMaxQuantity);
        editTextPrice = findViewById(R.id.editTextPrice);
        Button buttonSubmit = findViewById(R.id.buttonSubmit);

        dbPrice = new DBPrice(this);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String minQuantityStr = editTextMinQuantity.getText().toString().trim();
                String maxQuantityStr = editTextMaxQuantity.getText().toString().trim();
                String priceStr = editTextPrice.getText().toString().trim();

                if (minQuantityStr.isEmpty() || maxQuantityStr.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(AddPriceActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    int minQuantity = Integer.parseInt(minQuantityStr);
                    int maxQuantity = Integer.parseInt(maxQuantityStr);
                    double price = Double.parseDouble(priceStr);

                    boolean isInserted = dbPrice.insertPriceRange(minQuantity, maxQuantity, price);
                    if (isInserted) {
                        Toast.makeText(AddPriceActivity.this, "Price added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddPriceActivity.this, "Failed to add price. Try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
