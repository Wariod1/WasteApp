package com.example.wasteapp;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    DatabaseHelper myDb;
    TextView tvSignup;
    EditText editEmail, editPassword;
    Button btnLoginCollector, btnLoginResident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDb = new DatabaseHelper(this);

        editEmail = findViewById(R.id.editText_email);
        tvSignup = findViewById(R.id.tvSignup);
        editPassword = findViewById(R.id.editText_password);
        btnLoginCollector = findViewById(R.id.button_login_collector);
        btnLoginResident = findViewById(R.id.button_login_resident);

        btnLoginCollector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin("collector");
            }
        });

        btnLoginResident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin("resident");
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin(String role) {
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else {
            boolean isValid = myDb.checkUser(email, password, role);
            if (isValid) {
                Intent intent;
                if (role.equals("collector")) {
                    intent = new Intent(Login.this, Collector.class);
                } else {
                    intent = new Intent(Login.this, Resdashboard.class);
                }
                startActivity(intent);
                finish();  // Finish the current activity
            } else {
                Toast.makeText(Login.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
