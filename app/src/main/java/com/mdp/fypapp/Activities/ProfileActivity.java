package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mdp.fypapp.Model.User;
import com.mdp.fypapp.R;

public class ProfileActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private TextView rank, credit, type, email, phone;
    private Button logout;
    private ImageView back;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        rank = findViewById(R.id.rank);
        credit = findViewById(R.id.credit);
        type = findViewById(R.id.type);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        firebaseAuth = FirebaseAuth.getInstance();

        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("MyUsers").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.d(TAG, "onComplete: loading userinfo failed");
                }else{
                    Log.d(TAG, "onComplete: " + task.getResult());
                    User userInfo = task.getResult().getValue(User.class);
                    initData(userInfo);
                }
            }
        });



        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        back = findViewById(R.id.backToMain);
        back.bringToFront();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void signOut(){
        firebaseAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showDialog(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(ProfileActivity.this);
        dialog.setTitle("Are you sure to logout?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                signOut();
            }
        });
        dialog.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.show();
    }

    private void initData(User userInfo){
        rank.setText(userInfo.getRank());
        credit.setText(userInfo.getCredit());
        type.setText(userInfo.getType());
        email.setText(firebaseAuth.getCurrentUser().getEmail());
        phone.setText(userInfo.getPhone());
    }
}