package com.example.mobile_ass2_task2.Player.OngoingQuizzes;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_ass2_task2.Admin.ViewTournaments.UpdateQuizTournament;
import com.example.mobile_ass2_task2.Player.PlayQuiz.QuizGameActivity;
import com.example.mobile_ass2_task2.R;

public class OngoingQuizzesCVHolder extends RecyclerView.ViewHolder {

    CardView cvQuizCard;
    TextView txtViewCVQuizID, txtQuizName, txtQuizCategory, txtQuizDifficulty, txtQuizDateRange, txtAlreadyParticipated;
    RatingBar rbQuizRating;
    Button btnParticipateOngoingQuiz;
    public OngoingQuizzesCVHolder(@NonNull View itemView) {
        super(itemView);

        cvQuizCard = itemView.findViewById(R.id.cvViewQuizTournament);
        txtViewCVQuizID = itemView.findViewById(R.id.txtViewCVQuizID);
        txtQuizName = itemView.findViewById(R.id.txtViewCVQuizName);
        txtQuizCategory = itemView.findViewById(R.id.txtViewCVQuizCategory);
        txtQuizDifficulty = itemView.findViewById(R.id.txtViewCVQuizDifficulty);
        txtQuizDateRange = itemView.findViewById(R.id.txtViewCVQuizDateRange);
        txtAlreadyParticipated = itemView.findViewById(R.id.txtAlreadyParticipated);
        rbQuizRating = itemView.findViewById(R.id.rbViewCVQuizRating);
        btnParticipateOngoingQuiz = itemView.findViewById(R.id.btnParticipateOngoingQuiz);

        btnParticipateOngoingQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(cvQuizCard.getContext(), "Opening Activity: " + txtQuizName.getText().toString(), Toast.LENGTH_LONG).show();
                Log.d("OngoingQuiz", "TEST" + txtViewCVQuizID.getText().toString());
                Intent intent = new Intent(cvQuizCard.getContext(), QuizGameActivity.class);
                intent.putExtra("quizID", txtViewCVQuizID.getText().toString());
                cvQuizCard.getContext().startActivity(intent);
            }
        });

    }
}
