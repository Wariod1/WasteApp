package com.example.wasteapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editUsername, editEmail, editPassword, editConfirmPassword;
    Spinner spinnerRole;
    TextView tvLogin;
    Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        myDb = new DatabaseHelper(this);

        editUsername = findViewById(R.id.editText_username);
        tvLogin = findViewById(R.id.tvLogin);
        editEmail = findViewById(R.id.editText_email);
        editPassword = findViewById(R.id.editText_password);
        editConfirmPassword = findViewById(R.id.editText_confirm_password);
        spinnerRole = findViewById(R.id.spinner_role);
        btnSignUp = findViewById(R.id.button_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString();
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                String confirmPassword = editConfirmPassword.getText().toString();
                String role = spinnerRole.getSelectedItem().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = myDb.insertData(username, email, password, role);
                    if (isInserted) {
                        Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                        // Navigate to LoginActivity after successful sign up
                        Intent intent = new Intent(SignUpActivity.this, Login.class);
                        startActivity(intent);
                        finish();  // Finish the current activity
                    } else {
                        Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
}