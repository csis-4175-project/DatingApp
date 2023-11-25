package com.example.termproject_datingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termproject_datingapp.ChatActivity;
import com.example.termproject_datingapp.R;
import com.example.termproject_datingapp.UserModel.ChatRoomModel;
import com.example.termproject_datingapp.utils.AndroidUtil;
import com.example.termproject_datingapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatRoomModel, RecentChatRecyclerAdapter.ChatRoomModelViewHolder> {
    private Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatRoomModel> options, Context context) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public RecentChatRecyclerAdapter.ChatRoomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row, parent, false);
        return new RecentChatRecyclerAdapter.ChatRoomModelViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatRoomModelViewHolder holder, int position, @NonNull ChatRoomModel model) {
        //add the extra fields for the user
//        holder.userName.setText(model.getUsername());
//        holder.phoneNumber.setText(model.getPhone());
//        if(model.getUserId().equals(FirebaseUtil.currentUSerID())){
//            holder.userName.setText(model.getUsername() + "(me)");
//        }
    }

    class ChatRoomModelViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView phoneNumber;
        TextView lastMessage;
        ImageView profilePicture;

        public ChatRoomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_text);
            phoneNumber = itemView.findViewById(R.id.phone_text);
            profilePicture = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
