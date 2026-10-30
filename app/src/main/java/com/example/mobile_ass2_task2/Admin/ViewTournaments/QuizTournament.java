package com.example.mobile_ass2_task2.Admin.ViewTournaments;

import java.util.List;

public class QuizTournament {
    private String name;
    private String category;
    private String difficulty;
    private String start_date;
    private String end_date;
    private int quizRating;
    private List<String> quizQuestionIDs;
    private String documentID;

    public QuizTournament() {
    }

    public QuizTournament(String name, String category, String difficulty, String start_date, String end_date, int quizRating, List<String> quizQuestionIDs) {
        this.name = name;
        this.category = category;
        this.difficulty = difficulty;
        this.start_date = start_date;
        this.end_date = end_date;
        this.quizRating = quizRating;
        this.quizQuestionIDs = quizQuestionIDs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getQuizRating() {
        return quizRating;
    }

    public void setQuizRating(int quizRating) {
        this.quizRating = quizRating;
    }

    public List<String> getQuizQuestionIDs() {
        return quizQuestionIDs;
    }

    public void setQuizQuestionIDs(List<String> quizQuestionIDs) {
        this.quizQuestionIDs = quizQuestionIDs;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}
