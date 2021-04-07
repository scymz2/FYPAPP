package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mdp.fypapp.Adapter.OptionAdapter;
import com.mdp.fypapp.Model.Question;
import com.mdp.fypapp.Model.Quiz;
import com.mdp.fypapp.Model.Result;
import com.mdp.fypapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private List<Quiz> quizzes = null;
    private Map<String, Question> questions = null;
    private int index = 0;
    private int credit;

    TextView description, progress;
    RecyclerView optionList;
    Button previous, next, submit;
    ProgressBar progressBar;

    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        initViews();
        setUpFirestore();
    }

    private void setUpFirestore(){
        String date = getIntent().getStringExtra("DATE");
        Log.d(TAG, "setUpFirestore: " + date.toString());
        firebaseFirestore = FirebaseFirestore.getInstance();
        if(date != null) {
            firebaseFirestore.collection("answers").whereEqualTo("userId", firebaseUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        if(task.getResult() != null){
                            Toast.makeText(QuestionActivity.this, "You have already answer all the quizzes!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(QuestionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            });

            firebaseFirestore.collection("quizzes").whereEqualTo("title", date)
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
                            Toast.makeText(QuestionActivity.this, "No more quizzes!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(QuestionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            });

        }
    }

    private void addCredit(){
        // Read credit
        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("MyUsers").child(firebaseUser.getUid()).child("credit").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d(TAG, "onComplete: firebase");
                    String c = task.getResult().getValue().toString();
                    credit = Integer.valueOf(c);
                }else{
                    Log.d(TAG, "onFailure: firebase");
                }
            }
        });
        myRef = database.getReference();
        myRef.child("MyUsers").child(firebaseUser.getUid()).child("credit").setValue(String.valueOf(credit+10));
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
                uploadResult();
                addCredit();
                Intent intent = new Intent(QuestionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Toast.makeText(QuestionActivity.this, "Thank you for you patience!, your points have been credited ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadResult(){

        Map<String, String> answers = new HashMap<>();
        Result result = new Result();

        for (int i=0; i< questions.size(); i++){
            answers.put("question" + String.valueOf(i+1), questions.get("question" + String.valueOf(i+1)).getAnswer());
        }
        result.setAnswers(answers);
        result.setTitle(quizzes.get(0).getTitle());
        result.setUserId(firebaseUser.getUid());

        // Get current time
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String currentDate = simpleDateFormat.format(date);

        result.setTime(currentDate);

        firebaseFirestore.collection("answers").
                add(result)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onSuccess: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: ", e);
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