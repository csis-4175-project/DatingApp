package com.example.termproject_datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termproject_datingapp.models.GlobalAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private TextView textInputEmail, textInputPassword;
    private String email, password;
    private Button signInBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        textInputEmail = findViewById(R.id.sign_in_email);
        textInputPassword = findViewById(R.id.sign_in_password);
        signInBtn = findViewById(R.id.btn_sign_in);

        initFirebase();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInput();
                signIn();
            }
        });
    }

    private void initFirebase() {
        firebaseAuth = GlobalAuth.getFirebaseAuth();
    }

    private void loadInput() {
        email = textInputEmail.getText().toString();
        password = textInputPassword.getText().toString();
    }

    private void signIn() {
        try {
            veridateInput();
            signInByFirebase();
        } catch (Exception e) {
            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("SignInActivity", Log.getStackTraceString(e));
        }
    }

    private void veridateInput() throws Exception {
        if(email.isEmpty() || password.isEmpty()) {
            throw new Exception("Type your email and password");
        }
    }

    private void signInByFirebase() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, signIn -> {
                if (signIn.isSuccessful()) {
                    user = firebaseAuth.getCurrentUser();
                    checkEmailVerification();
                } else {
                    Toast.makeText(SignInActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void checkEmailVerification() {
        if(isEmailVerified()) {
            saveUser();
            navigateToMainActivity();
        } else {
            sendEmailVerification();
        }
    }

    private boolean isEmailVerified() {
        return user.isEmailVerified();
    }

    private void sendEmailVerification() {
        user.sendEmailVerification().addOnCompleteListener(sendEmail -> {
            if (sendEmail.isSuccessful()) {
                startActivity(new Intent(SignInActivity.this, SignUpSuccessActivity.class));
                finish();
            } else {
                Toast.makeText(SignInActivity.this, "Sending email failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        deleteAllPreviousActivity(intent);
        startActivity(intent);
        finish();
    }

    private void deleteAllPreviousActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    private void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", user.getUid());
        editor.putString("email", email);
        editor.putString("emailVerification", "verified");
        editor.apply();
    }
}