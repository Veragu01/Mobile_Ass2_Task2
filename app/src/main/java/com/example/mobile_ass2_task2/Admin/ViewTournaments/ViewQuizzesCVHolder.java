package com.example.mobile_ass2_task2.Admin.ViewTournaments;

import android.content.Intent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobile_ass2_task2.R;

public class ViewQuizzesCVHolder extends RecyclerView.ViewHolder {

    CardView cvQuizCard;
    TextView txtViewCVQuizID, txtQuizName, txtQuizCategory, txtQuizDifficulty, txtQuizDateRange;
    RatingBar rbQuizRating;
    public ViewQuizzesCVHolder(@NonNull View itemView) {
        super(itemView);

        cvQuizCard = itemView.findViewById(R.id.cvViewQuizTournament);
        txtViewCVQuizID = itemView.findViewById(R.id.txtViewCVQuizID);
        txtQuizName = itemView.findViewById(R.id.txtViewCVQuizName);
        txtQuizCategory = itemView.findViewById(R.id.txtViewCVQuizCategory);
        txtQuizDifficulty = itemView.findViewById(R.id.txtViewCVQuizDifficulty);
        txtQuizDateRange = itemView.findViewById(R.id.txtViewCVQuizDateRange);
        rbQuizRating = itemView.findViewById(R.id.rbViewCVQuizRating);


        cvQuizCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(cvQuizCard.getContext(), "Opening Activity: " + txtQuizName.getText().toString(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(cvQuizCard.getContext(), UpdateQuizTournament.class);
                intent.putExtra("docID", txtViewCVQuizID.getText().toString());
                cvQuizCard.getContext().startActivity(intent);
            }
        });

    }
}
