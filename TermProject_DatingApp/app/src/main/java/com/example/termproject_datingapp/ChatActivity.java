package com.example.termproject_datingapp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.termproject_datingapp.UserModel.ChatMessageModel;
import com.example.termproject_datingapp.UserModel.ChatRoomModel;
import com.example.termproject_datingapp.UserModel.UserModel;
import com.example.termproject_datingapp.adapter.ChatRecycler;
import com.example.termproject_datingapp.utils.AndroidUtil;
import com.example.termproject_datingapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import java.sql.Timestamp;
import java.util.Arrays;

public class ChatActivity extends AppCompatActivity {
    private UserModel otherUser;
    private String chatroomId;
    private ChatRoomModel chatRoomModel;
    private EditText messageInput;
    private ImageButton sendMessageButton;
    private ImageButton backButton;
    private TextView otherUserName;
    private RecyclerView recyclerView;
    private com.google.firebase.Timestamp timestamp;
    private ChatRecycler chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //to get user details
        //contains all user data
        otherUser = AndroidUtil.getUserModelFromIntent((getIntent()));
        //Connects the two users
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUSerID(),otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        sendMessageButton = findViewById(R.id.message_send_button);
        backButton = findViewById(R.id.backButton);
        otherUserName = findViewById(R.id.other_username);

        recyclerView = findViewById(R.id.chat_recycler_view);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //set name of the user your messaging
        //header will be username
        otherUserName.setText(otherUser.getUsername());
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = messageInput.getText().toString().trim();
                if(message.isEmpty()){
                    return;
                }
                sendMessageToUser(message);
            }
        });

        getOrCreateChatroomModel();
        setupChatRecyclerView();
    }

    private void setupChatRecyclerView(){
        //this code right now dosn't do much
        //needs firebase data to create query to get user information
        //The Query
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query, ChatMessageModel.class).build();

        chatAdapter = new ChatRecycler(options, getApplicationContext() );
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(chatAdapter);
        chatAdapter.startListening();
        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void sendMessageToUser(String message) {
        //setting chat room data
        chatRoomModel.setLastMessageTimestamp(timestamp.now());
        chatRoomModel.setLastMessageSenderId(FirebaseUtil.currentUSerID());
        chatRoomModel.setLastMessage(message);
        FirebaseUtil.getChatroomReference(chatroomId).set(chatRoomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message, FirebaseUtil.currentUSerID(), timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        //send the message to the database
                        if(task.isSuccessful()){
                            messageInput.setText("");
                        }
                    }
                });
    }

    private void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    chatRoomModel = task.getResult().toObject(ChatRoomModel.class);
                    if(chatRoomModel == null){
                        //first time chat
                        chatRoomModel = new ChatRoomModel(
                                chatroomId,
                                Arrays.asList(FirebaseUtil.currentUSerID(), otherUser.getUserId()),
                                timestamp.now(),
                                ""
                        );
                        FirebaseUtil.getChatroomReference(chatroomId).set(chatRoomModel);
                    }
                }
            }
        });
    }
}