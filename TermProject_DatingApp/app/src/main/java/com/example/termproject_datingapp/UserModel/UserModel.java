package com.example.termproject_datingapp.UserModel;

import com.google.firebase.Timestamp;

public class UserModel {

    private String userId;

    private String username;

    private Timestamp createdTimestamp;

    public UserModel(){

    }

    public UserModel( String username, String userId, Timestamp timestamp){
        //this.phone = phone;
        this.username = username;
        this.userId = userId;
        this.createdTimestamp = timestamp;
    }

//    public String getPhone() {
//        return phone;
//    }

//    public void setPhone(String phone) {
//        this.phone = phone;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

}
