package com.example.mobile_ass2_task2.Player.OngoingQuizzes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_ass2_task2.Admin.ViewTournaments.QuizTournament;
import com.example.mobile_ass2_task2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OngoingQuizzesCVAdapter extends RecyclerView.Adapter<OngoingQuizzesCVHolder> {
    List<QuizTournament> quizTournamentList;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    public OngoingQuizzesCVAdapter(List<QuizTournament> quizTournaments) {
        this.quizTournamentList=quizTournaments;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }


    @NonNull
    @Override
    public OngoingQuizzesCVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_player_ongoing_quiz_card, parent, false);
        return new OngoingQuizzesCVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OngoingQuizzesCVHolder holder, int position) {
        QuizTournament quiz = quizTournamentList.get(position);

        Log.d("OngoingQuiz", quiz.getDocumentID());
        holder.txtViewCVQuizID.setText(quiz.getDocumentID());
        holder.txtQuizName.setText(quiz.getName());
        holder.txtQuizCategory.setText(quiz.getCategory());
        holder.txtQuizDifficulty.setText(quiz.getDifficulty());
        holder.txtQuizDateRange.setText(quiz.getStart_date() + " - " + quiz.getEnd_date());
        holder.rbQuizRating.setRating(quiz.getQuizRating());

        // if player has participated already, button is hidden and "already participated" text is shown
        db.collection("player_quiz").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        if ( document.getId().contains(currentUser.getUid()) && document.getId().contains(quiz.getDocumentID()) ) {
                            holder.btnParticipateOngoingQuiz.setVisibility(View.GONE);
                            holder.txtAlreadyParticipated.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });




    }

    @Override
    public int getItemCount() {
        return quizTournamentList.size();
    }
}
