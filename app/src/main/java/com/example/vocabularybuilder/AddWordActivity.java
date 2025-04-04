package com.example.vocabularybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddWordActivity extends AppCompatActivity {
    EditText etWord, etMeaning;
    Button btnAddWord, btnBackToHome;
    WordDatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);

        etWord = findViewById(R.id.etWord);
        etMeaning = findViewById(R.id.etMeaning);
        btnAddWord = findViewById(R.id.btnAddWord);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        dbHelper = new WordDatabaseHelper(this);

        // Get user email from intent
        userEmail = getIntent().getStringExtra("email");

        btnAddWord.setOnClickListener(view -> {
            String word = etWord.getText().toString().trim();
            String meaning = etMeaning.getText().toString().trim();

            if (!word.isEmpty() && !meaning.isEmpty()) {
                dbHelper.addWord(userEmail, word, meaning);
                Toast.makeText(this, "Word Added!", Toast.LENGTH_SHORT).show();
                etWord.setText("");
                etMeaning.setText("");
            } else {
                Toast.makeText(this, "Please enter both word and meaning!", Toast.LENGTH_SHORT).show();
            }
        });

        btnBackToHome.setOnClickListener(view -> {
            finish();
        });
    }
}
