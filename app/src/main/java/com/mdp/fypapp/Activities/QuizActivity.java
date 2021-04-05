package com.mdp.fypapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mdp.fypapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class QuizActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    TextView skip;
    Button start;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

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
                Intent i = new Intent(QuizActivity.this, QuestionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("DATE", currentDate);
                Log.d(TAG, "onClick: " + currentDate);
                startActivity(i);
                finish();
            }
        });
    }
}