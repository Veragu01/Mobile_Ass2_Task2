package com.example.mobile_ass2_task2.Player.PlayQuiz;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobile_ass2_task2.Admin.ViewTournaments.QuizTournament;
import com.example.mobile_ass2_task2.Player.OngoingQuizzes.PlayerOngoingQuizzes;
import com.example.mobile_ass2_task2.Player.PlayerDashboard;
import com.example.mobile_ass2_task2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizGameActivity extends AppCompatActivity {

    TextView txtCorrectAnswers, txtIncorrectAnswers, txtQuestionNumber, txtQuestionTitle, txtAnswer1, txtAnswer2, txtAnswer3, txtAnswer4, txtQuizTitle, txtFinalScore;
    CardView crdAnswer1, crdAnswer2, crdAnswer3, crdAnswer4, crdQuizQuestion, crdFinalScore;
    RatingBar rbPlayerRateQuiz;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DocumentSnapshot questionDocument;
    String correctAnswer = "", quizTournamentID;
    int globalQuestionNumber = 1;
    int correctAnswerCount = 0, incorrectAnswerCount = 0;
    Button btnNextQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtQuizTitle = findViewById(R.id.txtQuizTitle);
        txtCorrectAnswers = findViewById(R.id.txtCorrectNo);
        txtIncorrectAnswers = findViewById(R.id.txtIncorrectNo);
        txtQuestionNumber = findViewById(R.id.txtQuizQuestionNo);
        txtQuestionTitle = findViewById(R.id.txtQuizQuestionTitle);
        txtFinalScore = findViewById(R.id.txtFinalScore);
        txtAnswer1 = findViewById(R.id.txtQuizQuestionAnswer1);
        txtAnswer2 = findViewById(R.id.txtQuizQuestionAnswer2);
        txtAnswer3 = findViewById(R.id.txtQuizQuestionAnswer3);
        txtAnswer4 = findViewById(R.id.txtQuizQuestionAnswer4);
        crdAnswer1 =findViewById(R.id.crdQuizQuestion1);
        crdAnswer2 =findViewById(R.id.crdQuizQuestion2);
        crdAnswer3 =findViewById(R.id.crdQuizQuestion3);
        crdAnswer4 =findViewById(R.id.crdQuizQuestion4);
        crdQuizQuestion = findViewById(R.id.crdQuestion);
        crdFinalScore = findViewById(R.id.crdFinalScore);
        rbPlayerRateQuiz = findViewById(R.id.rbPlayerRateQuiz);
        btnNextQuestion = findViewById(R.id.btnNextQuestion);

        rbPlayerRateQuiz.setRating(0);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        btnNextQuestion.setVisibility(View.GONE);

        Intent intent = getIntent();
        quizTournamentID = intent.getStringExtra("quizID"); // i.e. "QTid00"

        DocumentReference quizRef = db.collection("quizTournament").document(quizTournamentID);

        fillQuizFields(quizRef, globalQuestionNumber);

        crdAnswer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClickedAnswer(txtAnswer1.getText().toString(), crdAnswer1);
            }
        });

        crdAnswer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClickedAnswer(txtAnswer2.getText().toString(), crdAnswer2);
            }
        });

        crdAnswer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClickedAnswer(txtAnswer3.getText().toString(), crdAnswer3);
            }
        });

        crdAnswer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkClickedAnswer(txtAnswer4.getText().toString(), crdAnswer4);
            }
        });


        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCompat.setBackgroundTintList(crdAnswer1, ColorStateList.valueOf(ContextCompat.getColor(btnNextQuestion.getContext(), R.color.white)));
                ViewCompat.setBackgroundTintList(crdAnswer2, ColorStateList.valueOf(ContextCompat.getColor(btnNextQuestion.getContext(), R.color.white)));
                ViewCompat.setBackgroundTintList(crdAnswer3, ColorStateList.valueOf(ContextCompat.getColor(btnNextQuestion.getContext(), R.color.white)));
                ViewCompat.setBackgroundTintList(crdAnswer4, ColorStateList.valueOf(ContextCompat.getColor(btnNextQuestion.getContext(), R.color.white)));
                txtCorrectAnswers.setText(""+correctAnswerCount);
                txtIncorrectAnswers.setText(""+incorrectAnswerCount);
                txtQuestionTitle.setText("");
                globalQuestionNumber++;


                if (globalQuestionNumber<11) {
                    btnNextQuestion.setVisibility(View.GONE);
                    if (globalQuestionNumber==10){
                        btnNextQuestion.setText("Finish Quiz");
                    }
                    crdAnswer1.setVisibility(View.GONE);
                    crdAnswer2.setVisibility(View.GONE);
                    crdAnswer3.setVisibility(View.GONE);
                    crdAnswer4.setVisibility(View.GONE);
                    fillQuizFields(quizRef, globalQuestionNumber);
                } else if (globalQuestionNumber==11){
                    // show final score
                    crdAnswer1.setVisibility(View.GONE);
                    crdAnswer2.setVisibility(View.GONE);
                    crdAnswer3.setVisibility(View.GONE);
                    crdAnswer4.setVisibility(View.GONE);
                    crdQuizQuestion.setVisibility(View.GONE);
                    txtFinalScore.setText(correctAnswerCount+"/10");
                    crdFinalScore.setVisibility(View.VISIBLE);
                    btnNextQuestion.setText("Submit Attempt");
                    btnNextQuestion.setVisibility(View.VISIBLE);
                } else {

                    String userID = currentUser.getUid();
                    // quizID global variable

                    // create player_quiz entry to db
                    if (rbPlayerRateQuiz.getRating()==0) {
                        Toast.makeText(getApplicationContext(), "Please enter a quiz rating", Toast.LENGTH_SHORT).show();
                    } else {
                        createPlayer_Quiz(userID);
                    }

                }
            }
        });



    }

    private void checkClickedAnswer(String selectedAnswer, CardView selectedCard) {
        if (selectedAnswer.equals(correctAnswer)) {
//            selectedCard.setCardBackgroundColor(Color.RED);
            ViewCompat.setBackgroundTintList(selectedCard, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fadedGreen))
            );
            correctAnswerCount++;

        } else {
            ViewCompat.setBackgroundTintList(selectedCard, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fadedRed)));
            if (txtAnswer1.getText().equals(correctAnswer)) {
                ViewCompat.setBackgroundTintList(crdAnswer1, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fadedGreen)));
                incorrectAnswerCount++;
            } else if (txtAnswer2.getText().equals(correctAnswer)) {
                ViewCompat.setBackgroundTintList(crdAnswer2, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fadedGreen)));
                incorrectAnswerCount++;
            } else if (txtAnswer3.getText().equals(correctAnswer)) {
                ViewCompat.setBackgroundTintList(crdAnswer3, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fadedGreen)));
                incorrectAnswerCount++;
            } else if (txtAnswer4.getText().equals(correctAnswer)) {
                ViewCompat.setBackgroundTintList(crdAnswer4, ColorStateList.valueOf(ContextCompat.getColor(this, R.color.fadedGreen)));
                incorrectAnswerCount++;
            }
            // find actual correct card and make it green
        }

        btnNextQuestion.setVisibility(View.VISIBLE);


    }

    private void fillQuizFields(DocumentReference quizRef, int questionNumber) {

        quizRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    QuizTournament quiz = document.toObject(QuizTournament.class);
                    quiz.setDocumentID(document.getId());
                    List<String> questionIDs = quiz.getQuizQuestionIDs();
                    String questionID = "";
                    questionID = "QQid"+ questionNumber;
                    txtQuizTitle.setText(HtmlCompat.fromHtml(document.get("name").toString() ,HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                    Log.d("QuestionID", questionID);
                    for (String question : questionIDs) {
                        Log.d("QuestionID", question);
                        if (question.equals(questionID)) {
                            db.collection("quizQuestions").document(questionID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        // Set answers to list, shuffle list, set answers to cards
                                        questionDocument = task.getResult();
                                        txtQuestionTitle.setText(HtmlCompat.fromHtml(questionDocument.get("question").toString(),HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                                        txtQuestionNumber.setText("Q"+questionNumber);
                                        List<String> answerList = new ArrayList<String>();
                                        List<String> incorrectList = (List<String>) questionDocument.get("incorrect_answers");
//                                        Log.d("test", incorrectList.toString());
                                        Log.d("Firestore", "Path: " + document.getReference().getPath());
                                        answerList.addAll(incorrectList);
                                        correctAnswer = questionDocument.get("correct_answer").toString();
                                        answerList.add(correctAnswer);
                                        for (int i = 0; i < answerList.size(); i++) {
                                            answerList.set(i, HtmlCompat.fromHtml(answerList.get(i),HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                                        }
                                        Log.d("Firestore", "Data: " + answerList.size());
                                        correctAnswer = answerList.get(0);
                                        Collections.shuffle(answerList);
                                        crdAnswer1.setVisibility(View.VISIBLE);
                                        crdAnswer2.setVisibility(View.VISIBLE);
                                        crdAnswer3.setVisibility(View.VISIBLE);
                                        crdAnswer4.setVisibility(View.VISIBLE);
                                        if (answerList.size()==4) {
                                            txtAnswer1.setText(answerList.get(0));
                                            txtAnswer2.setText(answerList.get(1));
                                            txtAnswer3.setText(answerList.get(2));
                                            txtAnswer4.setText(answerList.get(3));
                                        } else if (answerList.size()==3) {
                                            txtAnswer1.setText(answerList.get(0));
                                            txtAnswer2.setText(answerList.get(1));
                                            txtAnswer3.setText(answerList.get(2));
                                            crdAnswer4.setVisibility(View.GONE);
                                        } else if (answerList.size()==2) {
                                            txtAnswer1.setText(answerList.get(0));
                                            txtAnswer2.setText(answerList.get(1));
                                            crdAnswer3.setVisibility(View.GONE);
                                            crdAnswer4.setVisibility(View.GONE);
                                        }

                                    }
                                }
                            });
                            break;
                        }
                    }


                }
            }
        });
    }

    private void createPlayer_Quiz(String userID) {

        // create player_quiz map
        Map<String, Object> newPlayer_Quiz = new HashMap<>();
        newPlayer_Quiz.put("player", userID);
        newPlayer_Quiz.put("quiz", quizTournamentID);
        newPlayer_Quiz.put("score", correctAnswerCount);
        newPlayer_Quiz.put("rating", (int)rbPlayerRateQuiz.getRating());

        // create player_quiz entry to db
        db.collection("player_quiz").document(userID+"_"+quizTournamentID).set(newPlayer_Quiz);
        Intent intent = new Intent(getApplicationContext(), PlayerDashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        startActivity(new Intent(btnNextQuestion.getContext(), PlayerOngoingQuizzes.class));

        db.collection("quizTournament").document(quizTournamentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    int existingRating = (int) document.get("quizRating");
                    if (existingRating == 0) {
                        db.collection("quizTournament").document(quizTournamentID).update("quizRating", (int)rbPlayerRateQuiz.getRating() );
                    } else {
                        List<Integer> ratings = new ArrayList<Integer>();
                        db.collection("player_quiz").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        String docID = document.getId();
                                        if (docID.contains(quizTournamentID)) {
                                            ratings.add((int)document.get("rating"));
//                                            ratings.add(((Number) document.get("rating")).intValue());
                                        }
                                    }
                                    int sum = 0;
                                    if (ratings.isEmpty()) {
                                        db.collection("quizTournament").document(quizTournamentID).update("quizRating", (int)rbPlayerRateQuiz.getRating() );
                                    } else {
                                        for (int rating : ratings) {
                                            sum += rating;
                                        }
                                        int averageRating = sum / ratings.size();

                                        db.collection("quizTournament").document(quizTournamentID).update("quizRating", averageRating);

                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}