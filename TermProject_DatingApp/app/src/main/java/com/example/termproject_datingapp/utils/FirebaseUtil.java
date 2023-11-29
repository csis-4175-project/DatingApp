package com.example.termproject_datingapp.utils;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static DocumentReference currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUSerID());
    }

    //not 100% on this code, could cause error in the future
    public static String currentUSerID(){
        return FirebaseAuth.getInstance().getUid();
    }

    //returns chatroom id
    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    //creates unique id for users
    public static String getChatroomId(String userId1, String userId2){
        if(userId1.hashCode() < userId2.hashCode()){
            return userId1+"_"+userId2;
        }else
            return userId2+"_"+userId1;
    }

    public static CollectionReference allChatRoomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatRoom(List<String> userId){
        if(userId.get(0).equals(FirebaseUtil.currentUSerID())){
           return allUserCollectionReference().document(userId.get(1));
        }else{
            return allUserCollectionReference().document(userId.get(0));
        }
    }

    public static String timestampToString(com.google.firebase.Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
}
