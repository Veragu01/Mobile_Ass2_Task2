package com.example.mobile_ass2_task2.Player;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobile_ass2_task2.Login;
import com.example.mobile_ass2_task2.Player.OngoingQuizzes.PlayerOngoingQuizzes;
import com.example.mobile_ass2_task2.Player.ViewQuizzes.PlayerViewQuizTournaments;
import com.example.mobile_ass2_task2.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerDashboard extends AppCompatActivity {

    CardView cvOngoingQuizzes, cvUpcomingQuizzes, cvPastQuizzes, cvParticipatedQuizzes, cvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_player_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cvOngoingQuizzes = findViewById(R.id.crdViewOngoingTournaments);
        cvUpcomingQuizzes = findViewById(R.id.crdViewUpcomingTournaments);
        cvPastQuizzes = findViewById(R.id.crdViewPastTournaments);
        cvParticipatedQuizzes = findViewById(R.id.crdViewParticipatedTournaments);
        cvLogout = findViewById(R.id.crdPlayerLogout);

        cvOngoingQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayerOngoingQuizzes.class);
                startActivity(intent);
            }
        });

        cvUpcomingQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayerViewQuizTournaments.class);
                intent.putExtra("quizFilter", "Upcomming");
                startActivity(intent);
            }
        });

        cvPastQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayerViewQuizTournaments.class);
                intent.putExtra("quizFilter", "Past");
                startActivity(intent);
            }
        });

        cvParticipatedQuizzes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayerViewQuizTournaments.class);
                intent.putExtra("quizFilter", "Participated");
                startActivity(intent);
            }
        });

        cvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }
}