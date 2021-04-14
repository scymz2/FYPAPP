package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mdp.fypapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuizActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    TextView skip;
    Button start;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        start = findViewById(R.id.button);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date(System.currentTimeMillis());
                String currentDate = simpleDateFormat.format(date);

                firebaseFirestore.collection("answers").whereEqualTo("userId", firebaseUser.getUid()).whereEqualTo("title", currentDate)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: sucessful");
                            if(task.getResult().isEmpty()){
                                Log.d(TAG, "onComplete: Haven't answered");

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                Date date = new Date(System.currentTimeMillis());
                                String currentDate = simpleDateFormat.format(date);

                                Intent i = new Intent(QuizActivity.this, QuestionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                i.putExtra("DATE", currentDate);
                                Log.d(TAG, "onClick: " + currentDate);
                                startActivity(i);

                            }else{
                                Toast.makeText(QuizActivity.this, "You have already answer all the questions", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }else{
                            finish();
                            Toast.makeText(QuizActivity.this, "Connection Error", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: Connection failed");
                        }
                    }
                });
            }
        });
    }
}