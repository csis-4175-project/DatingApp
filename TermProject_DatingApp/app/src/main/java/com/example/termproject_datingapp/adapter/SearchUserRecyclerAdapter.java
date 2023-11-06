package com.example.termproject_datingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.termproject_datingapp.R;
import com.example.termproject_datingapp.UserModel.UserModel;
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
        holder.phoneNumber.setText(model.getPhone());
        //this is for firebase
        //I will edit this when firebase part is finished
        //if(model.getUserId() == equals((currentId()))){
        //holder.userName.setText(model.getUsername()) + "(YOU)";
        //}
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView phoneNumber;
        ImageView profilePicture;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name_text);
            phoneNumber = itemView.findViewById(R.id.phone_text);
            profilePicture = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
