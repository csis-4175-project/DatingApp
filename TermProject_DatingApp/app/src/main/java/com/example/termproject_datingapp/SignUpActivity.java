package com.example.termproject_datingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termproject_datingapp.UserModel.UserModel;
import com.example.termproject_datingapp.adapters.ProgramSpinnerAdapter;
import com.example.termproject_datingapp.models.GlobalAuth;
import com.example.termproject_datingapp.models.User;
import com.example.termproject_datingapp.utils.FirebaseUtil;
import com.example.termproject_datingapp.utils.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView firstNameView, lastNameView, emailView, passwordView, confirmPasswordView;
    private Button createBtn;
    private Spinner programSpinner;
    private ProgramSpinnerAdapter adapter;
    private FirebaseAuth firebaseAuth;
    private  FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private User user;
    private UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstNameView = findViewById(R.id.sign_up_first_name);
        lastNameView = findViewById(R.id.sign_up_last_name);
        emailView = findViewById(R.id.sign_up_email);
        passwordView = findViewById(R.id.sign_up_password);
        confirmPasswordView = findViewById(R.id.sign_up_confirm_password);
        createBtn = findViewById(R.id.sign_up_create_btn);

        initSpinner();
        initFirebaseAuth();

        createBtn.setOnClickListener(v -> {
            user = loadInputToUserModel();
            signUp();
        });
    }

    private void setUserName(){
        String username = firstNameView.getText().toString();
        if(username.isEmpty() || username.length() < 3){
            firstNameView.setError("Username should be at least 3 letters long");
            return;
        }
        if(userModel!=null){
            userModel.setUsername(username);
            userModel.setCreatedTimestamp(createTimestamp());
        }else{
            //create user based on input
            userModel = new UserModel(username, FirebaseUtil.currentUSerID() , createTimestamp());
        }

        FirebaseUtil.currentUserDetails().set(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // do nothing
                }
            }
        });
    }

    private Timestamp createTimestamp() {
        // Create a timestamp for the current time
        return new Timestamp(new java.util.Date());
    }

    private void initSpinner() {
        programSpinner = findViewById(R.id.sign_up_program);
        adapter = new ProgramSpinnerAdapter(SignUpActivity.this);
        adapter.fillProgramSpinner(programSpinner);
    }

    private void initFirebaseAuth() {
        firebaseAuth = GlobalAuth.getFirebaseAuth();
    }

    private User loadInputToUserModel() {
        String firstName = firstNameView.getText().toString();
        String lastName = lastNameView.getText().toString();
        String email = emailView.getText().toString();
        String program = adapter.getProgram();
        String password = passwordView.getText().toString();
        String confirmPassword = confirmPasswordView.getText().toString();

        User user = new User(firstName, lastName, email, password, confirmPassword, program);
        return user;
    }

    private void signUp() {
        try {
            validateInput();
            createFirebaseAuthUser();
        } catch (Exception e) {
            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("signUp()", Log.getStackTraceString(e));
        }
    }

    private void validateInput() throws Exception {
        Validation.isValidName(user.getFirstName(), user.getLastName());
        Validation.isValidEmailFormat(user.getEmail());
        Validation.isValidProgramSelected(user.getProgram());
        Validation.isValidPassword(user.getPassword(), user.getConfirmPassword());
    }

    private void createFirebaseAuthUser() {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
            .addOnCompleteListener(this, createUser -> {
                if (createUser.isSuccessful()) {

                    setUserName(); //do not move this method

                    firebaseUser = firebaseAuth.getCurrentUser();
                    setIdToUserModel(firebaseUser.getUid());
                    // sendEmailVerification(); // comment out for testing
                    saveUserToDB();
                    saveUserToSharedPreferences();
                    navigateToMainActivity();
                } else {
                    Toast.makeText(SignUpActivity.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void setIdToUserModel(String userId) {
        user.setUserId(userId);
    }

    private void sendEmailVerification() {
        firebaseUser.sendEmailVerification().addOnCompleteListener(sendEmail -> {
            if (sendEmail.isSuccessful()) {
                Log.d("sendEmailVerification","Successfully sent email verification");
                 navigateToSignUpSuccessActivity();
            } else {
                Toast.makeText(SignUpActivity.this, "Sending email failed.", Toast.LENGTH_SHORT).show();
                Log.e("sendEmailVerification","failed to send email verification");
            }
        });
    }

    private void saveUserToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", user.getUserId());
        editor.putString("firstName", user.getFirstName());
        editor.putString("lastName", user.getLastName());
        editor.putString("email", user.getEmail());
        editor.putString("program", user.getProgram());
        editor.putString("emailVerification", "unverified");
        editor.apply();
    }

    private void saveUserToDB() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.child(user.getUserId()).setValue(user).addOnCompleteListener(savingData -> {
            if(savingData.isSuccessful()) {
                Log.d("saveUserToDB","successfully saved");
            } else {
                Log.d("saveUserToDB","process failed");
            }
        });
    }

    private void navigateToSignUpSuccessActivity() {
        startActivity(new Intent(SignUpActivity.this, SignUpSuccessActivity.class));
        finish();
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        deleteAllPreviousActivity(intent);
        startActivity(intent);
        finish();
    }

    private void deleteAllPreviousActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }
}