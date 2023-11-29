package com.example.termproject_datingapp.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.termproject_datingapp.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.termproject_datingapp.ChatActivity;
import com.example.termproject_datingapp.UserModel.ChatRoomModel;
import com.example.termproject_datingapp.UserModel.UserModel;
import com.example.termproject_datingapp.utils.AndroidUtil;
import com.example.termproject_datingapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {
     Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new ChatRoomModelViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        FirebaseUtil.getOtherUserFromChatRoom(model.getUserId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            boolean messageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUSerID());

                            UserModel otherUserModel = task.getResult().toObject(UserModel.class);
                            holder.userName.setText(otherUserModel.getUsername());
                            if(messageSentByMe){
                                holder.lastMessageText.setText("You: " + model.getLastMessage());
                            }else{
                                holder.lastMessageText.setText(model.getLastMessage());
                            }
                            holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(context, ChatActivity.class);
                                    //to access data in chat activity
                                    AndroidUtil.passUserModelAsIntent(intent, otherUserModel);
                                    intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }
                            });
                        }
                    }
                });
    }


    public class ChatRoomModelViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        //TextView phoneNumber;
        TextView lastMessageText;
        TextView lastMessageTime;
        //ImageView profilePicture;

        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_time_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_text);
            //profilePicture = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
