package com.example.termproject_datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EntryScreenActivity extends AppCompatActivity {

    Button signUpBtn;
    Button signInBtn;
    String userId, emailVerificationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_screen);

        loadUser();
        if(isLoggedIn()) {
            startActivity(new Intent(EntryScreenActivity.this, MainActivity.class));
            finish();
        };

        signUpBtn = findViewById(R.id.entry_sign_up_btn);
        signInBtn = findViewById(R.id.entry_sign_in_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntryScreenActivity.this, SignUpActivity.class));
            }
        });

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EntryScreenActivity.this, SignInActivity.class));
            }
        });
    }

    private void loadUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId", "NoUser");
        emailVerificationStatus = sharedPreferences.getString("emailVerification", "unverified");
    }

    private boolean isLoggedIn() {
        return !userId.equals("NoUser") && !emailVerificationStatus.equals("unverified");
    }
}