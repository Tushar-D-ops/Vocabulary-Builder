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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import java.util.Calendar;

public class
MainActivity extends AppCompatActivity {
    String userEmail;
    ProgressBar progressBarGoal;
    TextView tvGoalStatus;
    int wordsAddedToday = 0;
    boolean quizTakenToday = false;

    SharedPreferences sharedPreferences;
    final String PREF_NAME = "GoalPrefs";
    final String KEY_DATE = "lastDate";
    final String KEY_WORDS = "wordsToday";
    final String KEY_QUIZ = "quizTaken";

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

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        checkAndResetProgress();
        loadProgress();
        updateGoalProgress();

        btnProfile.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("email", userEmail);
            startActivity(intent);
        });

        btnAddWord.setOnClickListener(view -> {
            wordsAddedToday++;
            updateGoalProgress();
            saveProgress();
            checkIfGoalCompleted();
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
            saveProgress();
            checkIfGoalCompleted();
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

    private void checkIfGoalCompleted() {
        if (wordsAddedToday >= 2 && quizTakenToday) {
            setNextDayReminder();
        }
    }

    private void setNextDayReminder() {
        Intent intent = new Intent(this, ReminderReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 20); // 🔁 1 minute later for testing

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }


//    private void setNextDayReminder() {
//        Intent intent = new Intent(this, ReminderReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, 1);
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        if (alarmManager != null) {
//            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        }
//    }

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

    private void checkAndResetProgress() {
        long lastResetTime = sharedPreferences.getLong("lastResetTime", 0);
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastResetTime >= 20 * 1000) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("lastResetTime", currentTime);
            editor.putInt(KEY_WORDS, 0);
            editor.putBoolean(KEY_QUIZ, false);
            editor.apply();

            wordsAddedToday = 0;
            quizTakenToday = false;
        }
    }


//    private void checkAndResetProgress() {
//        String today = getCurrentDate();
//        String savedDate = sharedPreferences.getString(KEY_DATE, "");
//
//        if (!today.equals(savedDate)) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(KEY_DATE, today);
//            editor.putInt(KEY_WORDS, 0);
//            editor.putBoolean(KEY_QUIZ, false);
//            editor.apply();
//            wordsAddedToday = 0;
//            quizTakenToday = false;
//        }
//    }

    private String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return year + "-" + month + "-" + day;
    }

    private void loadProgress() {
        wordsAddedToday = sharedPreferences.getInt(KEY_WORDS, 0);
        quizTakenToday = sharedPreferences.getBoolean(KEY_QUIZ, false);
    }

    private void saveProgress() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_WORDS, wordsAddedToday);
        editor.putBoolean(KEY_QUIZ, quizTakenToday);
        editor.apply();
    }



}
