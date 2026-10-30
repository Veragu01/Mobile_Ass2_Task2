package com.example.mobile_ass2_task2.Admin.GetQuestionsAPI;

import java.util.ArrayList;
import java.util.List;

public class Questions {
    private int response;
    private List<Question> results;

    public int getQuestionsResponse() {
        return response;
    }

    public List<Question> getResults() {
        return results;
    }
}
