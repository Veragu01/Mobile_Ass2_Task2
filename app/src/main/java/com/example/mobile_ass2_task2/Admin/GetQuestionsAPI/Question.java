package com.example.mobile_ass2_task2.Admin.GetQuestionsAPI;

import java.util.List;

public class Question {
    private String type;
    private String difficulty;
    private String category;
    private String question;
    private String correctAnswer;
    private List<String> incorrectAnswers;

    public Question(String type, String difficulty, String category, String question, String correctAnswer, List<String> incorrectAnswers) {
        this.type = type;
        this.difficulty = difficulty;
        this.category = category;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.incorrectAnswers = incorrectAnswers;
    }

    public Question() {

    }

    public String getType() {
        return type;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getCategory() {
        return category;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrectAnswers;
    }
}
