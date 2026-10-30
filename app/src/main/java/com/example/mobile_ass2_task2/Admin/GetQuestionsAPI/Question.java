package com.example.mobile_ass2_task2.Admin.GetQuestionsAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Question {
    private String type;
    private String difficulty;
    private String category;
    private String question;
    private String correct_answer;
    private List<String> incorrect_answers;

    public Question(String type, String difficulty, String category, String question, String correctAnswer, List<String> incorrectAnswers) {
        this.type = type;
        this.difficulty = difficulty;
        this.category = category;
        this.question = question;
        this.correct_answer = correctAnswer;
        this.incorrect_answers = incorrectAnswers;
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
        return correct_answer;
    }

    public List<String> getIncorrectAnswers() {
        return incorrect_answers;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correct_answer = correctAnswer;
    }

    public void setIncorrectAnswers(List<String> incorrectAnswers) {
        this.incorrect_answers = incorrectAnswers;
    }
}
