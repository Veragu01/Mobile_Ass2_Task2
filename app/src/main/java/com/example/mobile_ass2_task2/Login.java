package com.example.mobile_ass2_task2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobile_ass2_task2.Admin.AdminDashboard;
import com.example.mobile_ass2_task2.Player.PlayerDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    EditText txtEmail, txtPassword;
    Button btnLogin;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtEmail = findViewById(R.id.etEmailInput);
        txtPassword = findViewById(R.id.etPasswordInput);
        btnLogin = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseFirestore.getInstance();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Error: Field/s are empty", Toast.LENGTH_LONG).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
                            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.get("user_type").equals("player")) {
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), PlayerDashboard.class);
                                            startActivity(intent);
                                            finish();
                                        } else if (document.get("user_type").equals("administrator")) {
                                            Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), AdminDashboard.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Login unsuccessful", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });


                        } else {
                            Toast.makeText(getApplicationContext(), "Error: No user matches provided credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });




    }
}