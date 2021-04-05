package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mdp.fypapp.Adapter.QuizAdapter;
import com.mdp.fypapp.Model.Question;
import com.mdp.fypapp.Model.Quiz;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    ActionBarDrawerToggle actionBarDrawerToggle;
    MaterialToolbar actionBar;
    DrawerLayout mainDrawer;
    QuizAdapter adapter;
    RecyclerView quizRecycleView;
    FloatingActionButton btmBot;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Quiz> quizList = new ArrayList<>();
    private String TAG = this.getClass().getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView(){
        actionBar = findViewById(R.id.appBar);
        mainDrawer = findViewById(R.id.mainDrawer);
        quizRecycleView = findViewById(R.id.quizRecycleView);
        btmBot = findViewById(R.id.btnBot);
        btmBot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Main2Activity.this, ChatbotActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        setUpFireStore();
        setUpDrawerLayout();
        setUpRecycleView();
    }


    private  void setUpRecycleView(){
        adapter = new QuizAdapter(quizList);
        quizRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
        quizRecycleView.setAdapter(adapter);
    }

    private void setUpDrawerLayout(){
        setSupportActionBar(actionBar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mainDrawer, R.string.app_name, R.string.app_name);
        actionBarDrawerToggle.syncState();
    }

    private void setUpFireStore(){
        db = FirebaseFirestore.getInstance();

        db.collection("quizzes")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    quizList.clear();
                    for (QueryDocumentSnapshot document: task.getResult()){
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        quizList.add(document.toObject(Quiz.class));
                    }
                    adapter.notifyDataSetChanged();
                }else{
                    Log.w(TAG, "Error getting documents", task.getException());
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}