package com.example.mobile_ass2_task2.Player.OngoingQuizzes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_ass2_task2.Admin.ViewTournaments.QuizTournament;
import com.example.mobile_ass2_task2.R;

import java.util.List;

public class OngoingQuizzesCVAdapter extends RecyclerView.Adapter<OngoingQuizzesCVHolder> {
    List<QuizTournament> quizTournamentList;

    public OngoingQuizzesCVAdapter(List<QuizTournament> quizTournaments) {
        this.quizTournamentList=quizTournaments;
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

        holder.txtViewCVQuizID.setText(quiz.getDocumentID());
        holder.txtQuizName.setText(quiz.getName());
        holder.txtQuizCategory.setText(quiz.getCategory());
        holder.txtQuizDifficulty.setText(quiz.getDifficulty());
        holder.txtQuizDateRange.setText(quiz.getStart_date() + " - " + quiz.getEnd_date());
        holder.rbQuizRating.setRating(quiz.getQuizRating());


    }

    @Override
    public int getItemCount() {
        return quizTournamentList.size();
    }
}
