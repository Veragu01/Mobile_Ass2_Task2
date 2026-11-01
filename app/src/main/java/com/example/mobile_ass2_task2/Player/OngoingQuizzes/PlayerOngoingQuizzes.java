package com.example.mobile_ass2_task2.Player.OngoingQuizzes;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_ass2_task2.Admin.ViewTournaments.QuizTournament;
import com.example.mobile_ass2_task2.Admin.ViewTournaments.ViewQuizzesCVAdapter;
import com.example.mobile_ass2_task2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PlayerOngoingQuizzes extends AppCompatActivity {
    RecyclerView rvOngoingQuizTournaments;
    List<QuizTournament> ongoingQuizTournaments;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_ongoing_quizzes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvOngoingQuizTournaments = findViewById(R.id.rvPlayerOngoingQuiz);
        db = FirebaseFirestore.getInstance();
        ongoingQuizTournaments = new ArrayList<QuizTournament>();

        OngoingQuizzesCVAdapter adapter = new OngoingQuizzesCVAdapter(ongoingQuizTournaments);

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

                            if ( !todayCalendar.before(startCalendar) && !todayCalendar.after(endCalendar)) {
//                            if ((todayCalendar.equals(startCalendar) || todayCalendar.after(startCalendar)) && (todayCalendar.before(endCalendar) || todayCalendar.equals(endCalendar))) {
                                QuizTournament quiz = document.toObject(QuizTournament.class);
                                quiz.setDocumentID(document.getId());
                                ongoingQuizTournaments.add(quiz);
                            }
                        }
                    }
                    Log.d("OngoingQuiz", ongoingQuizTournaments.toString());
                    adapter.notifyDataSetChanged();
                    Log.d("OngoingQuiz", "Recycler View updated with ongoing Quiz Tournaments");
//                    pbLoadingFruitRV.setVisibility(View.GONE);
//                    rv.setVisibility(View.VISIBLE);
                }
            }
        });

        rvOngoingQuizTournaments.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvOngoingQuizTournaments.setAdapter(adapter);



    }
}