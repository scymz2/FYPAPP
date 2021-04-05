package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mdp.fypapp.Adapter.OptionAdapter;
import com.mdp.fypapp.Model.Question;
import com.mdp.fypapp.Model.Quiz;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private List<Quiz> quizzes = new ArrayList<>();
    private Map<String, Question> questions = null;
    private int index = 0;

    TextView description, progress;
    RecyclerView optionList;
    Button previous, next, submit;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initViews();
        setUpFirestore();
    }

    private void setUpFirestore(){
        String date = getIntent().getStringExtra("DATE");
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        if(date != null) {
            firebaseFirestore.collection("quizzes").whereEqualTo("title", "11-12-2021")
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + "=>" + document.getData());
                            quizzes.add(document.toObject(Quiz.class));
                        }
                        if(quizzes != null){
                            questions = quizzes.get(0).getQuestions();
                            bindViews();
                        }else{
                            Toast.makeText(QuestionActivity.this, "No quiz today!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(QuestionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            });

        }
    }

    private void initViews(){
        previous = findViewById(R.id.btnPrevious);
        next = findViewById(R.id.btnNext);
        submit = findViewById(R.id.btnSubmit);
        progress = findViewById(R.id.progress);
        progressBar = findViewById(R.id.progressBar);

        previous.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index -= 1;
                bindViews();
                progress.setText(String.valueOf(index+1) + "/" + String.valueOf(questions.size()));
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index += 1;
                bindViews();
                progress.setText(String.valueOf(index+1) + "/" + String.valueOf(questions.size()));
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void bindViews(){

        previous.setVisibility(View.GONE);
        next.setVisibility(View.GONE);
        submit.setVisibility(View.GONE);

        if(index == 0){
            next.setVisibility(View.VISIBLE);
        }else if(index == questions.size()-1){
            submit.setVisibility(View.VISIBLE);
            previous.setVisibility(View.VISIBLE);
        }else{
            previous.setVisibility(View.VISIBLE);
        }

        description = findViewById(R.id.description);
        optionList = findViewById(R.id.optionList);
        description.setText(questions.get("question" + String.valueOf(index+1)).getDescription());
        OptionAdapter optionAdapter = new OptionAdapter(questions.get("question" + String.valueOf(index+1)));
        optionList.setLayoutManager(new LinearLayoutManager(this));
        optionList.setAdapter(optionAdapter);
        optionList.setHasFixedSize(true);

        progress.setText(String.valueOf(index+1) + "/" + String.valueOf(questions.size()));
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showDialog();
    }

    private void showDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(QuestionActivity.this);
        dialog.setTitle("Are you sure to exit quiz?");
        dialog.setMessage("Your answer is really important  for this research, please help us.");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(QuestionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }
}