package com.example.vocabularybuilder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ViewWordsActivity extends AppCompatActivity {
    ListView listViewWords;
    Button btnBackToHome;
    WordDatabaseHelper dbHelper;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_words);

        listViewWords = findViewById(R.id.listViewWords);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        dbHelper = new WordDatabaseHelper(this);

        userEmail = getIntent().getStringExtra("email");
        loadWords();

        btnBackToHome.setOnClickListener(view -> {
            finish();
        });
    }

    private void loadWords() {
        List<WordModel> wordModels = dbHelper.getAllWords(userEmail);
        ArrayList<String> wordsList = new ArrayList<>();

        for (WordModel model : wordModels) {
            wordsList.add(model.getWord() + " - " + model.getMeaning());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, wordsList);
        listViewWords.setAdapter(adapter);
    }
}
