package com.example.mobile_ass2_task2.Player.ViewQuizzes;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PlayerViewQuizzesCVAdapter extends RecyclerView.Adapter<PlayerViewQuizzesCVHolder> {
    List<QuizTournament> quizTournamentList;
    String quizFilter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    public PlayerViewQuizzesCVAdapter(List<QuizTournament> quizTournaments, String quizFilter) {
        this.quizTournamentList=quizTournaments;
        this.quizFilter=quizFilter;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }


    @NonNull
    @Override
    public PlayerViewQuizzesCVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.activity_player_view_quiz_card, parent, false);
        return new PlayerViewQuizzesCVHolder(view, quizFilter);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewQuizzesCVHolder holder, int position) {
        QuizTournament quiz = quizTournamentList.get(position);

        holder.txtViewCVQuizID.setText(quiz.getDocumentID());
        holder.txtQuizName.setText(quiz.getName());
        holder.txtQuizCategory.setText(quiz.getCategory());
        holder.txtQuizDifficulty.setText(quiz.getDifficulty());
        holder.txtQuizDateRange.setText(quiz.getStart_date() + " - " + quiz.getEnd_date());
        holder.rbQuizRating.setRating(quiz.getQuizRating());

        if (!quizFilter.equals("Participated") ) {
            holder.QuizScoreLayout.setVisibility(View.GONE);
        } else {

            db.collection("player_quiz").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot PQDoc : task.getResult()) {
                            if ( PQDoc.getId().contains(mAuth.getCurrentUser().getUid()) && PQDoc.getId().contains(quiz.getDocumentID()) ) {
                                holder.txtQuizScore.setText(PQDoc.get("score").toString()+ "/10");
                            }
                        }
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return quizTournamentList.size();
    }
}
