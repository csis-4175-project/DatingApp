package com.example.termproject_datingapp.utils;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.termproject_datingapp.UserModel.UserModel;
import com.example.termproject_datingapp.models.User;

public class AndroidUtil {

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    //pass data to chat activity to get user information
    //display user details in text activity
    public static void passUserModelAsIntent(Intent intent, UserModel model){
        intent.putExtra("username", model.getUsername());
        //intent.putExtra("phone", model.getPhone());
        intent.putExtra("userId", model.getUserId());

    }

    public static UserModel getUserModelFromIntent(Intent intent){
        UserModel userModel = new UserModel();
        userModel.setUsername(intent.getStringExtra("username"));
        //userModel.setPhone(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }
}
