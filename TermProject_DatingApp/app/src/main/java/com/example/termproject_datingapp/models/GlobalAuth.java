package com.example.termproject_datingapp.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GlobalAuth {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public GlobalAuth() {
    }

    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseAuth.getCurrentUser();
    }
}
