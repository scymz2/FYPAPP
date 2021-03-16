package com.mdp.fypapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import com.mdp.fypapp.Adapter.ChatAdapter;
import com.mdp.fypapp.Interfaces.BotReply;
import com.mdp.fypapp.Model.Chat;
import com.mdp.fypapp.R;
import com.mdp.fypapp.helpers.SendMessageInBg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChatbotActivity extends AppCompatActivity implements BotReply {

    RecyclerView recyclerView;
    TextView username;
    ImageView imageView;
    ImageButton sendBtn;
    EditText editText;
    List<Chat> chatList = new ArrayList<>();

    ChatAdapter chatAdapter;

    //dialog flow
    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String uuid = UUID.randomUUID().toString();
    private String TAG = "Chatbotactivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);

        imageView = findViewById(R.id.robotAvatar);
        username = findViewById(R.id.robotName);
        sendBtn = findViewById(R.id.btn_send);
        editText = findViewById(R.id.text_send);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        chatAdapter = new ChatAdapter(this, chatList);
        recyclerView.setAdapter(chatAdapter);

        sendBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String message = editText.getText().toString();
                if(!message.isEmpty()){
                    chatList.add(new Chat(0, message,false));
                    editText.setText("");
                    sendMessageToBot(message);
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(recyclerView.getLayoutManager())
                .scrollToPosition(chatList.size()-1);
                }else{
                    Toast.makeText(ChatbotActivity.this, "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        setUpBot();
    }

    private void setUpBot(){
        try{
            InputStream stream = this.getResources().openRawResource(R.raw.credential);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            Log.d(TAG, "projectId: " + projectId);
        }catch (Exception e){
            Log.d(TAG, "setUpBot" + e.getMessage());
        }
    }

    private void sendMessageToBot(String message){
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();
    }


    @Override
    public void callback(DetectIntentResponse response) {
        if(response != null){
            String botReply = response.getQueryResult().getFulfillmentText();
            if(!botReply.isEmpty()){
                chatList.add(new Chat(1,botReply,true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(recyclerView.getLayoutManager()).scrollToPosition(chatList.size()-1);
            }else{
                Toast.makeText(this,"something went wrong", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }
}