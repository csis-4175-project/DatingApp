package com.example.termproject_datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.termproject_datingapp.models.GlobalAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpSuccessActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_success);
        firebaseAuth = GlobalAuth.getFirebaseAuth();
        user = firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user.reload();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkEmailVerification();
    }

    private void checkEmailVerification() {
        if(user.isEmailVerified()) {
            saveVerificationStatus();
            navigateToMainActivity();
        } else {
            reloadUserByReloadingActivity();
        }
    }

    private void saveVerificationStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailVerification", "verified");
        editor.apply();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpSuccessActivity.this, MainActivity.class);
        deleteAllPreviousActivity(intent);
        startActivity(intent);
        finish();
    }

    private void deleteAllPreviousActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void reloadUserByReloadingActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }, 3000);
    }
}