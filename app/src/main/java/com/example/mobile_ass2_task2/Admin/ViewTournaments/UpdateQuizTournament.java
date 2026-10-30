package com.example.mobile_ass2_task2.Admin.ViewTournaments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobile_ass2_task2.Admin.AdminDashboard;
import com.example.mobile_ass2_task2.Admin.ViewQuizTournaments;
import com.example.mobile_ass2_task2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UpdateQuizTournament extends AppCompatActivity {

    TextView txtQuizCategory, txtQuizDifficulty, txtQuizQuestions;
    EditText etQuizName;
    RatingBar rbQuizRating;
    DatePicker dpStartDate, dpEndDate;
    FirebaseFirestore db;
    String questionsString = "";
    Button btnUpdateQuiz, btnDeleteQuiz, btnCancel;
    DialogInterface.OnClickListener dialogClickListener;
    Calendar startDateCalendar, endDateCalendar;
    int startYear, startMonth, startDay, endYear, endMonth, endDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_quiz_tournament);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etQuizName = findViewById(R.id.etUpdateQuizTournamentName);
        rbQuizRating = findViewById(R.id.rbUpdateQuizRating);
        txtQuizCategory = findViewById(R.id.txtUpdateQuizTournamentCategory);
        txtQuizDifficulty = findViewById(R.id.txtUpdateQuizTournamentDifficulty);
        dpStartDate = findViewById(R.id.dpUpdateQuizStartDate);
        dpEndDate = findViewById(R.id.dpUpdateQuizEndDate);
        txtQuizQuestions = findViewById(R.id.txtUpdateQuizQuestionsList);
        btnUpdateQuiz = findViewById(R.id.btnUpdateQuizTournament);
        btnDeleteQuiz = findViewById(R.id.btnDeleteQuizTournament);
        btnCancel = findViewById(R.id.btnCancelUpdateQuiz);

        Intent intent = getIntent();
        String quizFirestoreID = intent.getStringExtra("docID");

        db = FirebaseFirestore.getInstance();

        db.collection("quizTournament").document(quizFirestoreID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();

                    etQuizName.setText(document.get("name").toString());
                    txtQuizCategory.setText(document.get("category").toString());
                    txtQuizDifficulty.setText(document.get("difficulty").toString());
                    String startDate = document.get("start_date").toString();
                    String endDate = document.get("end_date").toString();
                    rbQuizRating.setRating(Integer.parseInt(document.get("quizRating").toString()));
                    List<String> quizQuestionIDs = new ArrayList<String>();
                    quizQuestionIDs = (List<String>) document.get("quizQuestionIDs");

                    Log.d("Update Quiz", quizQuestionIDs.toString());


                    // split date string
                    String[] startDateSplit = startDate.split("-", 3);
                    Log.d("Update Quiz", "Start Date: " + startDateSplit[0] +" "+ startDateSplit[1] +" "+ startDateSplit[2]);
                    startDateCalendar = Calendar.getInstance();

                    // set start date spinner
                    startYear = Integer.parseInt(startDateSplit[0].trim());
                    startMonth = Integer.parseInt(startDateSplit[1].trim()) -1;
                    startDay = Integer.parseInt(startDateSplit[2].trim());
                    dpStartDate.updateDate(startYear, startMonth, startDay);
                    startDateCalendar.set(startYear, startMonth, startDay);

                    // set end date spinner
                    String[] endDateSplit = endDate.split("-", 3);
                    Log.d("Update Quiz", "Start Date: " + endDateSplit[0] +" "+ endDateSplit[1] +" "+ endDateSplit[2]);
                    endDateCalendar = Calendar.getInstance();
                    endYear = Integer.parseInt(endDateSplit[0].trim());
                    endMonth = Integer.parseInt(endDateSplit[1].trim()) -1;
                    endDay = Integer.parseInt(endDateSplit[2].trim());
                    dpEndDate.updateDate(endYear, endMonth, endDay);
                    endDateCalendar.set(endYear, endMonth, endDay);

                    txtQuizQuestions.setText("");
                    questionsString = "";
                    String[] questionTexts = new String[quizQuestionIDs.size()];
                    int[] completed = {0};

                    for (int i=0; i<quizQuestionIDs.size(); i++) {
                        String questionID = quizQuestionIDs.get(i);
                        int j = i+1;
                        db.collection("quizQuestions").document(questionID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    String correctAnswer = HtmlCompat.fromHtml(document.get("correct_answer").toString(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                                    List<String> incorrectAnswers = (List<String>) document.get("incorrect_answers");


                                    questionTexts[j-1] = "Q"+ j +": " + HtmlCompat.fromHtml(String.valueOf(document.get("question")),
                                            HtmlCompat.FROM_HTML_MODE_LEGACY).toString() + "\n";
                                    questionTexts[j-1] += "    Correct Answer: " + correctAnswer +" \n";

                                    for (int k = 0; k < incorrectAnswers.size(); k++) {
                                        String answer = HtmlCompat.fromHtml(incorrectAnswers.get(k), HtmlCompat.FROM_HTML_MODE_LEGACY).toString();
                                        questionTexts[j - 1] += "    Incorrect Answer" + (k+1) + ": " + answer + "\n";
                                    }
                                    questionTexts[j-1] += "\n";

                                    completed[0]++;
                                    if (completed[0] == questionTexts.length) {
                                        for (String questionText : questionTexts) {
                                            questionsString += questionText;
                                        }
                                        txtQuizQuestions.setText(questionsString);
                                    }
                                }

                            }
                        });

                    }


                }
            }
        });

        btnUpdateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etQuizName.getText().toString().trim();
                String startDate = dpStartDate.getYear() + "-" + (dpStartDate.getMonth()+1) + "-" + dpStartDate.getDayOfMonth();
                String endDate =  dpEndDate.getYear() + "-" + (dpEndDate.getMonth()+1) + "-" + dpEndDate.getDayOfMonth();
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Error: Name field is empty", Toast.LENGTH_LONG).show();
                } else if (startDateCalendar.after(endDateCalendar)) {
                    Toast.makeText(getApplicationContext(), "Error: Start Date must be before End Date", Toast.LENGTH_LONG).show();
                } else {
                    db.collection("quizTournament").document(quizFirestoreID).update("name", name);
                    db.collection("quizTournament").document(quizFirestoreID).update("start_date", startDate);
                    db.collection("quizTournament").document(quizFirestoreID).update("end_date", endDate);

                    Toast.makeText(getApplicationContext(), "Succ: Start Date must be before End Date", Toast.LENGTH_LONG).show();

                    Intent dashboardIntent = new Intent(btnUpdateQuiz.getContext(), AdminDashboard.class);
                    dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(dashboardIntent);
                    startActivity(new Intent(btnUpdateQuiz.getContext(), ViewQuizTournaments.class));
                }


            }
        });

        btnDeleteQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        switch (choice) { // switch to determine outcome based on what dialog button is clicked
                            case DialogInterface.BUTTON_POSITIVE:
                                db.collection("quizTournament").document(quizFirestoreID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "Quiz Deleted", Toast.LENGTH_SHORT).show();

                                        Intent dashboardIntent = new Intent(btnDeleteQuiz.getContext(), AdminDashboard.class);
                                        dashboardIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(dashboardIntent);
                                        startActivity(new Intent(btnDeleteQuiz.getContext(), ViewQuizTournaments.class));
                                    }
                                });
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();

                        }
                    }
                };
                new MaterialAlertDialogBuilder(UpdateQuizTournament.this)
                        .setMessage("Are you sure you want to delete this Quiz Tournament?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener)
                        .show();







            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }
}