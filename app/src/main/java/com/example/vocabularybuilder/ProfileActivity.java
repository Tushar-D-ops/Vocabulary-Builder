package com.example.vocabularybuilder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    TextView tvUserName, tvUserEmail;
    Button btnLogout, btnBackToHome;
    UserDatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnBackToHome = findViewById(R.id.btnBackToHome);

        dbHelper = new UserDatabaseHelper(this);

        // Get email passed from LoginActivity
        userEmail = getIntent().getStringExtra("email");

        if (userEmail != null) {
            Cursor cursor = dbHelper.getUserByEmail(userEmail);
            if (cursor != null && cursor.moveToFirst()) {
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String name = email.split("@")[0];

                tvUserName.setText("User Name: " + name);
                tvUserEmail.setText("Email: " + email);
                cursor.close();
            }
        }

        btnLogout.setOnClickListener(view -> {
            // Simply redirect to login (no session logic since we're not storing login state)
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
            startActivity(intent);
            finish();
        });

        btnBackToHome.setOnClickListener(view -> {
            finish(); // Closes this activity and returns to Home
        });
    }
}
