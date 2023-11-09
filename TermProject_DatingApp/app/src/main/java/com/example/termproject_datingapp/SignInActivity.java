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
import com.example.termproject_datingapp.models.User;
import com.example.termproject_datingapp.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.Map;

public class SignInActivity extends AppCompatActivity {

    private TextView textInputEmail, textInputPassword;
    private String email, password;
    private Button signInBtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

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
            Validation.validateSignInInput(email, password);
            signInByFirebase();
        } catch (Exception e) {
            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("SignInActivity", Log.getStackTraceString(e));
        }
    }

    private void signInByFirebase() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, signIn -> {
                if (signIn.isSuccessful()) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    retrieveUserFromDB();
                    checkEmailVerification();
                } else {
                    Toast.makeText(SignInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void retrieveUserFromDB() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, String> userMap = (Map<String, String>)snapshot.getValue();
                    User userModel = createUserModel(userMap);
                    saveUserToSharedPreferences(userModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("onCancelled", "Retrieving data cancelled");
            }
        });
    }

    private User createUserModel(Map<String, String> userMap) {
        User user = new User();
        user.setUserId(userMap.get("userId"));
        user.setFirstName(userMap.get("firstName"));
        user.setLastName(userMap.get("lastName"));
        user.setEmail(userMap.get("email"));
        user.setProgram(userMap.get("program"));

        return user;
    }

    private void saveUserToSharedPreferences(User user) {
        SharedPreferences sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", user.getUserId());
        editor.putString("firstName", user.getFirstName());
        editor.putString("lastName", user.getLastName());
        editor.putString("email", user.getEmail());
        editor.putString("program", user.getProgram());
        editor.apply();
    }

    private void checkEmailVerification() {
        if(isEmailVerified()) {
            saveEmailVerificationToSharedPreferences("verified");
            navigateToMainActivity();
        } else {
            sendEmailVerification();
        }
    }

    private boolean isEmailVerified() {
        return firebaseUser.isEmailVerified();
    }

    private void saveEmailVerificationToSharedPreferences(String status) {
        SharedPreferences sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("emailVerification", status);
        editor.apply();
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

    private void sendEmailVerification() {
        firebaseUser.sendEmailVerification().addOnCompleteListener(sendEmail -> {
            if (sendEmail.isSuccessful()) {
                navigateToSignUpSuccessActivity();
            } else {
                Toast.makeText(SignInActivity.this, "Sending email failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToSignUpSuccessActivity() {
        startActivity(new Intent(SignInActivity.this, SignUpSuccessActivity.class));
        finish();
    }
}