package com.example.mobile_ass2_task2.Admin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import java.util.List;

public class ViewQuizTournaments extends AppCompatActivity {

    RecyclerView rvViewQuizTournaments;
    List<QuizTournament> quizTournaments;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_quiz_tournaments);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvViewQuizTournaments = findViewById(R.id.rvViewQuizTournaments);
        db = FirebaseFirestore.getInstance();
        quizTournaments = new ArrayList<QuizTournament>();


        ViewQuizzesCVAdapter adapter = new ViewQuizzesCVAdapter(quizTournaments);
        db.collection("quizTournament").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("View Quiz", "Test 0");
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getId().contains("QTid")) {
                            QuizTournament quiz = document.toObject(QuizTournament.class);
                            quiz.setDocumentID(document.getId());
                            quizTournaments.add(quiz);
                        }

                    }
                    adapter.notifyDataSetChanged();
                    Log.d("View Quiz", "Test 1");
//                    pbLoadingFruitRV.setVisibility(View.GONE);
//                    rv.setVisibility(View.VISIBLE);
                }
            }
        });


        rvViewQuizTournaments.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rvViewQuizTournaments.setAdapter(adapter);


    }
}