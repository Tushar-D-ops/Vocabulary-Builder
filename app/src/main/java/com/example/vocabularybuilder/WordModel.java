package com.example.vocabularybuilder;

public class WordModel {
    private String word;
    private String meaning;

    public WordModel(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }
}
