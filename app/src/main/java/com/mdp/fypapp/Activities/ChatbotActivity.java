package com.mdp.fypapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.mdp.fypapp.Adapter.ChatAdapter;
import com.mdp.fypapp.Http.HttpRequest;
import com.mdp.fypapp.Model.Chat;
import com.mdp.fypapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatbotActivity extends AppCompatActivity{

    private List<Chat> list;
    private ListView listview;
    private EditText input;
    private Button send;
    private ChatAdapter chatAdapter;
    private Chat chatMessage = null;
    private ImageView backToMain;
    private ImageView microphone;
    private EditText inputText;

    private static final int RECOGNIZER_RESULT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        microphone = findViewById(R.id.microphone);
        inputText = findViewById(R.id.chat_input_message);

        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speach to text");
                startActivityForResult(i, RECOGNIZER_RESULT);
            }
        });

        backToMain = findViewById(R.id.backToMain);
        backToMain.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        initListener();
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        if(requestCode == RECOGNIZER_RESULT && requestCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            inputText.setText(matches.get(0).toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        listview = findViewById(R.id.listview);
        input = findViewById(R.id.chat_input_message);
        send = findViewById(R.id.send);
    }

    private void initListener() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.send:
                        chat();
                        break;
                }
            }
        });
    }

    private void initData(){
        list = new ArrayList<Chat>();
        list.add(new Chat("Hi, What is up?", new Date(), Chat.Type.INCOUNT));
                chatAdapter = new ChatAdapter(list);
        listview.setAdapter(chatAdapter);
        chatAdapter.notifyDataSetChanged();
    }

    private void chat(){
        final String send_message = input.getText().toString().trim();
        if (TextUtils.isEmpty(send_message)) {
            Toast.makeText(ChatbotActivity.this, "please type something...",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Chat sendChat = new Chat();
        sendChat.setMessage(send_message);
        sendChat.setDate(new Date());
        sendChat.setType(Chat.Type.OUTCOUNT);
        list.add(sendChat);
        chatAdapter.notifyDataSetChanged();
        input.setText("");

        new Thread(){
            public void run(){
                Chat chat = HttpRequest.sendMessage(send_message);
                Message message = new Message();
                message.what = 0X1;
                message.obj = chat;
                handler.sendMessage(message);
            };
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0x1) {
                if (msg.obj != null) {
                    chatMessage = (Chat) msg.obj;
                }
                //更新数据
                list.add(chatMessage);
                chatAdapter.notifyDataSetChanged();
            }
        };
    };




}