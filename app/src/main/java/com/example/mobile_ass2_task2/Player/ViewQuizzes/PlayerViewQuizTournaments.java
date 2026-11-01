package com.example.mobile_ass2_task2.Player.ViewQuizzes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_ass2_task2.Admin.ViewTournaments.QuizTournament;
import com.example.mobile_ass2_task2.Player.OngoingQuizzes.OngoingQuizzesCVAdapter;
import com.example.mobile_ass2_task2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlayerViewQuizTournaments extends AppCompatActivity {

    TextView txtPageTitle; //, txtPlayerScore, txtPlayerScoreTitle;
    RecyclerView rvQuizTournaments;
    List<QuizTournament> quizTournaments;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    String quizFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_view_quiz_tournaments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtPageTitle = findViewById(R.id.txtPlayerViewQuizTitle);
//        txtPlayerScore = findViewById(R.id.txtParticipatedPlayerScore);
//        txtPlayerScoreTitle = findViewById(R.id.txtParticipatedPlayerScoreTitle);

        rvQuizTournaments = findViewById(R.id.rvPlayerViewQuiz);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        quizTournaments = new ArrayList<QuizTournament>();

        Intent intent = getIntent();
        quizFilter = intent.getStringExtra("quizFilter");
        switch (quizFilter) {
            case "Upcoming":
                txtPageTitle.setText("Upcoming Quiz Tournaments");
//                txtPlayerScore.setVisibility(View.GONE);
//                txtPlayerScoreTitle.setVisibility(View.GONE);
                break;
            case "Past":
                txtPageTitle.setText("Past Quiz Tournaments");
//                txtPlayerScore.setVisibility(View.GONE);
//                txtPlayerScoreTitle.setVisibility(View.GONE);
                break;
            case "Participated":
                txtPageTitle.setText("Participated Quiz Tournaments");
//                txtPlayerScore.setText("0 / 10");
                break;
            default:
        }

        PlayerViewQuizzesCVAdapter adapter = new PlayerViewQuizzesCVAdapter(quizTournaments, quizFilter);

        db.collection("quizTournament").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("View Quiz", "Test 0");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().contains("QTid")) {
                            Calendar todayCalendar = Calendar.getInstance();

                            String startDateString = document.get("start_date").toString();
                            Calendar startCalendar = Calendar.getInstance();
                            String[] startDateSplit = startDateString.split("-", 3);
                            startCalendar.set(Integer.parseInt(startDateSplit[0]), Integer.parseInt(startDateSplit[1])-1, Integer.parseInt(startDateSplit[2]));

                            String endDateString = document.get("end_date").toString();
                            Calendar endCalendar = Calendar.getInstance();
                            String[] endDateSplit = endDateString.split("-", 3);
                            endCalendar.set(Integer.parseInt(endDateSplit[0]), Integer.parseInt(endDateSplit[1])-1, Integer.parseInt(endDateSplit[2]));

                            if ( quizFilter.equals("Upcoming") && todayCalendar.before(startCalendar)) {
                                QuizTournament quiz = document.toObject(QuizTournament.class);
                                quiz.setDocumentID(document.getId());
                                quizTournaments.add(quiz);
                            } else if ( quizFilter.equals("Past") && todayCalendar.after(endCalendar) ) {
                                QuizTournament quiz = document.toObject(QuizTournament.class);
                                quiz.setDocumentID(document.getId());
                                quizTournaments.add(quiz);
                            }
                            else if ( quizFilter.equals("Participated")) { // && check if player_quiz item exists   ) {

                                db.collection("player_quiz").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot player_quizDocument : task.getResult()) {
                                                String docID = player_quizDocument.getId();
                                                if (docID.contains(document.getId()) && docID.contains(mAuth.getCurrentUser().getUid())) {
                                                    QuizTournament quiz = document.toObject(QuizTournament.class);
                                                    quiz.setDocumentID(document.getId());
                                                    quizTournaments.add(quiz);
                                                    Log.d("OngoingQuiz", "Recycler View updated with Quiz Tournaments. " + quizTournaments.size() + " " + quizFilter);

                                                    break;
                                                }
                                            }
                                            adapter.notifyDataSetChanged();
                                        }

                                    }
                                });


                            }

                        }
                    }
                    Log.d("OngoingQuiz", "Recycler View updated with Quiz Tournaments. " + quizTournaments.size() + " " + quizFilter);

//                    pbLoadingFruitRV.setVisibility(View.GONE);
//                    rv.setVisibility(View.VISIBLE);
                }
            }
        });

        rvQuizTournaments.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvQuizTournaments.setAdapter(adapter);


    }
}