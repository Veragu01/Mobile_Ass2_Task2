package com.example.mobile_ass2_task2.Admin.GetQuestionsAPI;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuestionController implements Callback <Questions> {
    final String BASE_URL = "https://opentdb.com/";
    FirebaseFirestore db;
    Questions questions;
    String quizName, quizCategory, quizDifficulty, startDate, endDate, tournamentID;
    int quizRating;
    List<String> quizQuestionIDs;

    public void start(String apiURL, String quizName, String quizCategory, String quizDifficulty, int quizRating, String startCalendar, String endCalendar, String tournamentID) {
        this.quizName = quizName;
        this.quizCategory=quizCategory;
        this.quizDifficulty=quizDifficulty;
        this.quizRating=quizRating;
        this.startDate=startCalendar;
        this.endDate=endCalendar;
        this.tournamentID=tournamentID;
        db = FirebaseFirestore.getInstance();
        Log.d("Question_API", BASE_URL+apiURL);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        QuestionsAPI questionAPI = retrofit.create(QuestionsAPI.class);
        Call<Questions> call = questionAPI.getQuestions(apiURL);
        call.enqueue(this);


    }


    @Override
    public void onResponse(Call<Questions> call, Response<Questions> response) {
        if (response.isSuccessful() && response.body() != null) {
            questions = response.body();
            if (questions.getResults() != null) {
                //has questions data. pass 'questions' somewhere
                Log.d("Question_API", questions.getResults().toString());
                List<Question> apiQuestions = questions.getResults();
                CollectionReference questionsColRef = db.collection("quizQuestions");
                questionsColRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int firstQuestionID = task.getResult().size();

                            WriteBatch batch = db.batch();
                            quizQuestionIDs = new ArrayList<>();

                            for (int i = 0; i < apiQuestions.size(); i++) {
                                Question question = apiQuestions.get(i);
                                String questionID = "QQid" + (firstQuestionID + i);
                                quizQuestionIDs.add(questionID);

                                Map<String, Object> newQuestion = new HashMap<>();
                                newQuestion.put("type", question.getType());
                                newQuestion.put("difficulty", question.getDifficulty());
                                newQuestion.put("category", question.getCategory());
                                newQuestion.put("question", question.getQuestion());
                                newQuestion.put("correct_answer", question.getCorrectAnswer());
                                newQuestion.put("incorrect_answers", question.getIncorrectAnswers());

                                batch.set(db.collection("quizQuestions").document(questionID),newQuestion);
                            }
                            Map<String, Object> newQuizTournament = new HashMap<>();
                            newQuizTournament.put("name", quizName);
                            newQuizTournament.put("category", quizCategory);
                            newQuizTournament.put("difficulty", quizDifficulty );
                            newQuizTournament.put("start_date", startDate );
                            newQuizTournament.put("end_date", endDate );
                            newQuizTournament.put("quizRating", quizRating);
                            newQuizTournament.put("quizQuestionIDs", quizQuestionIDs);

                            batch.set(db.collection("quizTournament").document(tournamentID), newQuizTournament);
                            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Log.d("Firestore", "Questions and tournament saved");
                                }
                            });




                        }
                    }
                });


//                Map<String, Object> newQuestion = new HashMap<>();
//                newQuestion.put("name", type);
//                newQuestion.put("category", difficulty);
//                newQuestion.put("difficulty", quizDifficulty );
//                newQuestion.put("start_date", (dpQuizStartDate.getYear() +"-"+ dpQuizStartDate.getMonth() +"-"+ dpQuizStartDate.getDayOfMonth()) );
//                newQuestion.put("end_date", (dpQuizEndDate.getYear() +"-"+ dpQuizEndDate.getMonth() +"-"+ dpQuizEndDate.getDayOfMonth()) );
//                newQuestion.put("quizRating", quizRating);
//                newQuestion.put("quizQuestionIDs", "");




            } else {
                Log.d("Question_API", "Error get Question data");
            }
        } else {
            Log.d("Question_API", "Request failed");

        }
    }

    @Override
    public void onFailure(Call<Questions> call, Throwable t) {
        Log.d("Question_API", "On Failure: Error getting Questions", t);
    }
}
