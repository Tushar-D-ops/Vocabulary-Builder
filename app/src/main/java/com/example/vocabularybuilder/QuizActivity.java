package com.example.vocabularybuilder;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    TextView tvQuestion, tvScore;
    Button btnOption1, btnOption2, btnOption3, btnOption4, btnNext, btnBackToHome;
    WordDatabaseHelper dbHelper;
    String correctMeaning;
    List<WordModel> wordList;
    int currentQuestionIndex = 0;
    int score = 0;


    Button[] optionButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        tvQuestion = findViewById(R.id.tvQuestion);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        btnOption3 = findViewById(R.id.btnOption3);
        btnOption4 = findViewById(R.id.btnOption4);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        btnNext = findViewById(R.id.btnNext);
        tvScore = findViewById(R.id.tvScore);

        optionButtons = new Button[]{btnOption1, btnOption2, btnOption3, btnOption4};

        dbHelper = new WordDatabaseHelper(this);
        String userEmail = getIntent().getStringExtra("email");
        wordList = dbHelper.getAllWords(userEmail); // assumes you have getAllWords(String email)

        if (wordList.size() < 4) {
            Toast.makeText(this, "Please add at least 4 words!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadNextQuestion();

        for (Button btn : optionButtons) {
            btn.setOnClickListener(view -> checkAnswer((Button) view));
        }

        btnNext.setOnClickListener(view -> {
            currentQuestionIndex++;
            if (currentQuestionIndex >= wordList.size()) {
                Toast.makeText(this, "Quiz completed! Your score: " + score + "/" + wordList.size(), Toast.LENGTH_LONG).show();
                currentQuestionIndex = 0;
                score = 0;
                tvScore.setText("Score: " + score);
            }
            loadNextQuestion();
        });

        btnBackToHome.setOnClickListener(view -> {
            finish();
        });
    }

    private void loadNextQuestion() {
        // Reset UI
        for (Button btn : optionButtons) {
            btn.setEnabled(true);
            btn.setBackgroundColor(Color.parseColor("#E0E0E0")); // light grey
        }
        btnNext.setVisibility(View.GONE);

        // Pick a word as question
        WordModel currentWord = wordList.get(currentQuestionIndex);
        String questionWord = currentWord.getWord();
        correctMeaning = currentWord.getMeaning();

        tvQuestion.setText("What is the meaning of: " + questionWord + "?");

        // Prepare options
        List<String> options = new ArrayList<>();
        options.add(correctMeaning);

        // Add 3 wrong meanings
        Random random = new Random();
        while (options.size() < 4) {
            int index = random.nextInt(wordList.size());
            String wrongMeaning = wordList.get(index).getMeaning();
            if (!options.contains(wrongMeaning)) {
                options.add(wrongMeaning);
            }
        }

        // Shuffle and set buttons
        Collections.shuffle(options);
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options.get(i));
        }
    }

    private void checkAnswer(Button selectedButton) {
        String selected = selectedButton.getText().toString();

        for (Button btn : optionButtons) {
            btn.setEnabled(false);
        }

        if (selected.equals(correctMeaning)) {
            selectedButton.setBackgroundColor(Color.parseColor("#4CAF50")); // green
            score++;
        } else {
            selectedButton.setBackgroundColor(Color.parseColor("#F44336")); // red
            for (Button btn : optionButtons) {
                if (btn.getText().toString().equals(correctMeaning)) {
                    btn.setBackgroundColor(Color.parseColor("#4CAF50")); // green
                    break;
                }
            }
        }

        tvScore.setText("Score: " + score);

        btnNext.setVisibility(View.VISIBLE);
    }
}
