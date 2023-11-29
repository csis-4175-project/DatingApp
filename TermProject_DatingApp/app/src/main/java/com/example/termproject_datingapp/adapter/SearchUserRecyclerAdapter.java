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
import com.example.termproject_datingapp.UserModel.UserModel;
import com.example.termproject_datingapp.utils.AndroidUtil;
import com.example.termproject_datingapp.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.auth.User;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    private Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row, parent, false);
        return new UserModelViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        //add the extra fields for the user
        holder.userName.setText(model.getUsername());
//        if(model.getUserId().equals(FirebaseUtil.currentUSerID())){
//            holder.userName.setText(model.getUsername()+" (Me)");
//        }

        //start chatting with user from here
        //sends users to chat activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                //to access data in chat activity
                AndroidUtil.passUserModelAsIntent(intent, model);
                intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        //TextView phoneNumber;
        ImageView profilePicture;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_text);
            //phoneNumber = itemView.findViewById(R.id.phone_text);
            profilePicture = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
