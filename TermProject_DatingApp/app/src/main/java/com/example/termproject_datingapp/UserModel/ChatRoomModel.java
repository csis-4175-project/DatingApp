package com.example.termproject_datingapp.UserModel;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

public class ChatRoomModel {
    String chatroomId;
    List<String> userId;
    com.google.firebase.Timestamp lastMessageTimestamp;
    String lastMessageSenderId;

    public ChatRoomModel(){

    }

    //time stamp might cause a error in the future
    //Making mental note about this
    public ChatRoomModel(String chatroomId, List<String> userId, com.google.firebase.Timestamp lastMessageTimestamp, String lastMessageSenderId) {
        this.chatroomId = chatroomId;
        this.userId = userId;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
    }


    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public com.google.firebase.Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp( com.google.firebase.Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

}
