package com.example.termproject_datingapp.models;

import com.google.firebase.auth.FirebaseAuth;

public class GlobalAuth {
    private static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public GlobalAuth() {
    }

    public static FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
