package com.example.vocabularybuilder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class
MainActivity extends AppCompatActivity {
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the email from LoginActivity or RegisterActivity
        userEmail = getIntent().getStringExtra("email");

        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnAddWord = findViewById(R.id.btnAddWord);
        Button btnViewWords = findViewById(R.id.btnViewWords);
        Button btnQuiz = findViewById(R.id.btnQuiz);
        Button btnReminder = findViewById(R.id.btnReminder);

        btnProfile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        btnAddWord.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
            intent.putExtra("email", userEmail);  // ✅ Pass user email
            startActivity(intent);
        });

        btnViewWords.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ViewWordsActivity.class);
            intent.putExtra("email", userEmail);  // ✅ Pass user email
            startActivity(intent);
        });

        btnQuiz.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, QuizActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        btnReminder.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        requestNotificationPermission();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }
}
