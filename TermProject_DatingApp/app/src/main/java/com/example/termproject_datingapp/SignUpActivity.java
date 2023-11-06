package com.example.termproject_datingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.termproject_datingapp.Adapters.ProgramSpinnerAdapter;
import com.example.termproject_datingapp.models.GlobalAuth;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private TextView firstNameView, lastNameView, emailView, passwordView, confirmPasswordView;
    private String firstName, lastName, email, program, password, confirmPassword;
    private Button createBtn;
    private Spinner programSpinner;
    private ProgramSpinnerAdapter adapter;
    private FirebaseAuth firebaseAuth;

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
        initFirebase();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void initSpinner() {
        programSpinner = findViewById(R.id.sign_up_program);
        adapter = new ProgramSpinnerAdapter(SignUpActivity.this);
        adapter.fillProgramSpinner(programSpinner);
    }

    private void initFirebase() {
        firebaseAuth = GlobalAuth.getFirebaseAuth();
    }

    private void signUp() {
        try {
            validateInput();
            createUser();
            saveUserToSharedPreferences();
        } catch (Exception e) {
            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("signUp()", Log.getStackTraceString(e));
        }
    }

    private void validateInput() throws Exception {
        loadInput();
        isValidName();
        isValidEmailFormat();
        isValidProgramSelected();
        isValidPassword();
    }

    private void loadInput() {
        firstName = firstNameView.getText().toString();
        lastName = lastNameView.getText().toString();
        email = emailView.getText().toString();
        program = adapter.getProgram();
        password = passwordView.getText().toString();
        confirmPassword = confirmPasswordView.getText().toString();
    }

    private void isValidName() throws Exception {
        if(firstName.isEmpty() || lastName.isEmpty()) throw new Exception("Enter your name");
    }

    private void isValidEmailFormat() throws Exception {
        String[] usernameAndDomainName = email.split("@");

        if(usernameAndDomainName.length != 2) {
            throw new Exception("Use proper student email provided by douglas college");
        }

        String username = usernameAndDomainName[0];
        String domainName = usernameAndDomainName[1];
        // username should be at least 2 long string
        // since the email constructed by last name and initial letter of first name
        if(username.length() <= 1 && domainName.equals("student.douglascollege.ca")) {
            throw new Exception("Use proper student email provided by douglas college");
        };
    }

    private void isValidProgramSelected() throws Exception {
        if(program.isEmpty()) {
            throw new Exception("Program is not selected");
        };
    }

    private void isValidPassword() throws Exception {
        isSecurePassword(password);
        if(!password.equals(confirmPassword)) throw new Exception("Password doesn't match");
    }

    private void isSecurePassword(String password) throws Exception {
        // include at least one upper case, lower case, digit and special character and more at least 8 characters long
        String strongPasswordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()-_+=<>?]).{8,}$";
        Pattern pattern = Pattern.compile(strongPasswordPattern);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) throw new Exception("Password must include a mix of uppercase and lowercase letters, at least one digit, one special character (e.g., !@#$%^&*), and be at least 8 characters long.");
    }

    private void createUser() {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, createUser -> {
                if (createUser.isSuccessful()) {
                    sendEmailVerification();
                } else {
                    Toast.makeText(SignUpActivity.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(sendEmail -> {
            if (sendEmail.isSuccessful()) {
                startActivity(new Intent(SignUpActivity.this, SignUpSuccessActivity.class));
                finish();
            } else {
                Toast.makeText(SignUpActivity.this, "Sending email failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserToSharedPreferences() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("userId", user.getUid());
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putString("program", program);
        editor.putString("emailVerification", "unverified");
        editor.apply();
    }
}