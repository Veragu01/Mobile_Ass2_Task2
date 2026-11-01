package com.example.mobile_ass2_task2.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobile_ass2_task2.Admin.GetQuestionsAPI.QuestionController;
import com.example.mobile_ass2_task2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class CreateNewTournament extends AppCompatActivity {

    EditText txtQuizName;
    Spinner spnrQuizCategory, spnrQuizDifficulty;
    DatePicker dpQuizStartDate, dpQuizEndDate;
//    RatingBar rbQuizRating;
    Button btnCreateQuiz;
    List<String> categoryList, categoryAPIStringList, difficultyList, difficultyAPIStringList;
    String quizName, quizCategory, quizDifficulty, startDate, endDate, APIString;
    int quizRating;
    FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_new_tournament);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtQuizName = findViewById(R.id.etNewQuizName);
        spnrQuizCategory = findViewById(R.id.spnrNewQuizCategory);
        spnrQuizDifficulty = findViewById(R.id.spnrNewQuizDifficulty);
        dpQuizStartDate = findViewById(R.id.dpNewQuizStartDate);
        dpQuizEndDate = findViewById(R.id.dpNewQuizEndDate);
//        rbQuizRating = findViewById(R.id.rbTournamentRating);
        btnCreateQuiz = findViewById(R.id.btnCreateQuiz);
        db = FirebaseFirestore.getInstance();

//        rbQuizRating.setRating(0);

        difficultyList = new ArrayList<String>();
        difficultyList.add("Any Difficulty");
        difficultyList.add("Easy");
        difficultyList.add("Medium");
        difficultyList.add("Hard");

        difficultyAPIStringList = new ArrayList<String>();
        difficultyAPIStringList.add("");
        difficultyAPIStringList.add("&difficulty=easy");
        difficultyAPIStringList.add("&difficulty=medium");
        difficultyAPIStringList.add("&difficulty=hard");

        categoryList = new ArrayList<String>();
        categoryList.add("Any Category");
        categoryList.add("General Knowledge");
        categoryList.add("Entertainment: Film");
        categoryList.add("Entertainment: Video Games");
        categoryList.add("Science & Nature");
        categoryList.add("Sports");
        categoryList.add("Geography");
        categoryList.add("History");
        categoryList.add("Art");
        categoryList.add("Animals");
        categoryList.add("Vehicles");

        categoryAPIStringList = new ArrayList<String>();
        categoryAPIStringList.add("");
        categoryAPIStringList.add("&category=9");
        categoryAPIStringList.add("&category=11");
        categoryAPIStringList.add("&category=15");
        categoryAPIStringList.add("&category=17");
        categoryAPIStringList.add("&category=21");
        categoryAPIStringList.add("&category=22");
        categoryAPIStringList.add("&category=23");
        categoryAPIStringList.add("&category=25");
        categoryAPIStringList.add("&category=27");
        categoryAPIStringList.add("&category=28");



        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categoryList);
        adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrQuizCategory.setAdapter((adapterCategory));

        ArrayAdapter<String> adapterDifficulty = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, difficultyList);
        adapterDifficulty.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnrQuizDifficulty.setAdapter((adapterDifficulty));


        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizName = txtQuizName.getText().toString().trim();
                quizCategory = spnrQuizCategory.getSelectedItem().toString();
                quizDifficulty = spnrQuizDifficulty.getSelectedItem().toString();
                quizRating = 0;
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.set(dpQuizStartDate.getYear(), dpQuizStartDate.getMonth(), dpQuizStartDate.getDayOfMonth());

                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(dpQuizEndDate.getYear(), dpQuizEndDate.getMonth(), dpQuizEndDate.getDayOfMonth());
                Log.d("Test", endCalendar.toString());
                Log.d("Test", endCalendar.before(startCalendar) + "");

                if (checkFields(quizName, startCalendar, endCalendar) ) {
                    // fields correct
                    // Fields include name, category, difficulty, start date, end date and rating ( 1 to 5)
                    CollectionReference tournamentCollection = db.collection("quizTournament");
                    tournamentCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                String tournamentID = "QTid" + task.getResult().size();
                                String startDateString = dpQuizStartDate.getYear() +"-"+ (dpQuizStartDate.getMonth()+1) +"-"+ dpQuizStartDate.getDayOfMonth();
                                String endDateString = dpQuizEndDate.getYear() +"-"+ (dpQuizEndDate.getMonth()+1) +"-"+ dpQuizEndDate.getDayOfMonth();
                                getQuestions(spnrQuizCategory.getSelectedItemPosition(), spnrQuizDifficulty.getSelectedItemPosition(),quizName, quizCategory, quizDifficulty, quizRating, startDateString, endDateString, tournamentID);
                            }
                        }
                    });
                } else {
                    // fields incorrect
                }
            }
        });
    }

    private boolean checkFields(String quizName, Calendar startCalendar, Calendar endCalendar) {
        boolean fieldsCorrect = true;
        if (quizName.isEmpty() ) {
            Toast.makeText(getApplicationContext(), "Error: Please enter a Quiz Name", Toast.LENGTH_SHORT).show();
            fieldsCorrect = false;

        } else if (!startCalendar.before(endCalendar)) {
            Toast.makeText(getApplicationContext(), "Error: End date must be after start date", Toast.LENGTH_SHORT).show();
            fieldsCorrect = false;

        }
        return fieldsCorrect;
    }

    private void getQuestions(int categoryPos, int difficultyPos, String quizName, String quizCategory, String quizDifficulty, int quizRating, String startCalendar, String endCalendar, String tournamentID) {
        //using the opentdb API get 10 questions matching input and add them to the firestore individually using dynamically created id's
        // https://opentdb.com/api.php?amount=10&category=21&difficulty=easy category 21 is sports, will need to assign each category to a number


//        APIString = "https://opentdb.com/api.php?amount=10" + categoryAPIStringList.get(categoryPos) + difficultyAPIStringList.get(difficultyPos);
        APIString = "api.php?amount=10"+categoryAPIStringList.get(categoryPos) + difficultyAPIStringList.get(difficultyPos);
        new QuestionController().start(APIString, quizName, quizCategory, quizDifficulty, quizRating, startCalendar, endCalendar, tournamentID);






    }

}