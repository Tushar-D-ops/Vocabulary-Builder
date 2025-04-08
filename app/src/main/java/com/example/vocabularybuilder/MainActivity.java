package com.example.vocabularybuilder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class
MainActivity extends AppCompatActivity {
    String userEmail;
    ProgressBar progressBarGoal;
    TextView tvGoalStatus;
    int wordsAddedToday = 0;
    boolean quizTakenToday = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBarGoal = findViewById(R.id.progressBarGoal);
        tvGoalStatus = findViewById(R.id.tvGoalStatus);

        updateGoalProgress();

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
            wordsAddedToday++;
            updateGoalProgress();
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
            quizTakenToday = true;
            updateGoalProgress();
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

    private void updateGoalProgress() {
        int progress = Math.min(wordsAddedToday, 2) + (quizTakenToday ? 1 : 0);
        progressBarGoal.setProgress(progress);
        tvGoalStatus.setText(progress + "/3 steps completed");
    }

}
