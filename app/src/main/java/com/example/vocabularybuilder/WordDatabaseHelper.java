package com.example.vocabularybuilder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class WordDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "words.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_WORDS = "words";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_WORD = "word";
    private static final String COLUMN_MEANING = "meaning";

    public WordDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_WORD + " TEXT, " +
                COLUMN_MEANING + " TEXT)";
        db.execSQL(CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
        onCreate(db);
    }

    public void addWord(String email, String word, String meaning) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_WORD, word);
        values.put(COLUMN_MEANING, meaning);
        db.insert(TABLE_WORDS, null, values);
        db.close();
    }

    public ArrayList<String> getAllWords(String email) {
        ArrayList<String> wordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORDS,
                new String[]{COLUMN_WORD, COLUMN_MEANING},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String word = cursor.getString(0);
                String meaning = cursor.getString(1);
                wordList.add(word + " - " + meaning);
            }
            cursor.close();
        }
        db.close();
        return wordList;
    }
}
