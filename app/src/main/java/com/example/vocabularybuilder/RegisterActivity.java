package com.example.vocabularybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText etRegEmail, etRegPassword;
    Button btnRegister;
    UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new UserDatabaseHelper(this);

        // 👇 Pre-fill the input fields if values are passed from LoginActivity
        Intent intent = getIntent();
        String prefilledEmail = intent.getStringExtra("email");
        String prefilledPassword = intent.getStringExtra("password");

        if (prefilledEmail != null) etRegEmail.setText(prefilledEmail);
        if (prefilledPassword != null) etRegPassword.setText(prefilledPassword);

        btnRegister.setOnClickListener(view -> {
            String email = etRegEmail.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show();
            } else {
                boolean registered = dbHelper.registerUser(email, password);
                if (registered) {
                    Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();

                    // 👇 Send back the entered values to LoginActivity
                    Intent returnIntent = new Intent(this, LoginActivity.class);
                    returnIntent.putExtra("email", email);
                    returnIntent.putExtra("password", password);
                    startActivity(returnIntent);
                    finish();
                } else {
                    Toast.makeText(this, "Registration failed. Email may already exist.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
