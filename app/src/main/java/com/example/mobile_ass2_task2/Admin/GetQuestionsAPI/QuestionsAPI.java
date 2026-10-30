package com.example.mobile_ass2_task2.Admin.GetQuestionsAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface QuestionsAPI {
    @GET
    Call<Questions> getQuestions(@Url String url);
}
