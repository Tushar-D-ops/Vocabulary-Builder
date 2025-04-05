package com.example.vocabularybuilder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnLogin, btnRegister;
    UserDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_login);

        SharedPreferences sharedPref = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        boolean isLoggedIn = sharedPref.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("email", sharedPref.getString("email", ""));
            startActivity(intent);
            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        dbHelper = new UserDatabaseHelper(this);

        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter all details!", Toast.LENGTH_SHORT).show();
            } else {
                boolean loggedIn = dbHelper.loginUser(email, password);
                if (loggedIn) {
                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPref1 = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref1.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("email", email);
                    editor.apply();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(this, "Invalid credentials. Please register.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, RegisterActivity.class));

                }
            }
        });

        btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });


    }
}
