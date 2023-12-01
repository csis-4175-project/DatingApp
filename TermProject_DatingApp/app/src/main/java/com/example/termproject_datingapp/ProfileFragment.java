package com.example.termproject_datingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.termproject_datingapp.databinding.FragmentFirstBinding;
import com.example.termproject_datingapp.databinding.FragmentProfileBinding;
import com.example.termproject_datingapp.models.Profile;
import com.example.termproject_datingapp.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private String userId, firstName, lastName, program, gender, bio, pictureUrl, dateOfBirth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataFromSharedPref();
        retrieveProfileDataFromDB();

        binding.editBuutton.setOnClickListener(v -> ((FragmentActivity) getContext())
            .getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.main_frame_layout, new EditProfileFragment()).addToBackStack(null)
            .commit());

        binding.profileLogout.setOnClickListener(v -> logout());
    }

    private void getDataFromSharedPref() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("config_file", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","null");
        firstName = sharedPreferences.getString("firstName","null");
        lastName = sharedPreferences.getString("lastName","null");
        program = sharedPreferences.getString("program","null");
    }

    private void retrieveProfileDataFromDB() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Profile");
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Map<String, String> profileMap = (Map<String, String>)snapshot.getValue();
                    gender = profileMap.get("gender");
                    pictureUrl = profileMap.get("profilePicUrl");
                    dateOfBirth = profileMap.get("dateOfBirth");
                    bio = profileMap.get("bio");
                } else {
                    gender = "?";
                    bio = "User has not written BIO yet";
                }
                setProfileDataToView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("onCancelled", "Retrieving data cancelled");
            }
        });
    }

    private void setProfileDataToView() {
        binding.profileName.setText(firstName + " " + lastName);
        setGenderAndAge();
        setProfileImage();
        binding.program.setText(program);
        if(bio == null || bio.isEmpty()) {
            binding.bio.setText("User has not written BIO yet");
        } else {
            binding.bio.setText(bio);
        }

    }

    private void setGenderAndAge() {
        String age = calcAge();
        if(gender.equals("?")) {
            binding.GenderAndAge.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gender_hidden)));
        } else if(gender.equals("M")) {
            binding.GenderAndAge.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gender_man)));
        } else if(gender.equals("W")) {
            binding.GenderAndAge.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gender_woman)));
        }
        binding.GenderAndAge.setText(gender + " " + age);
    }
    private String calcAge() {
        if(dateOfBirth == null) {
            return "??";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/M/d");
        LocalDate birthdate = LocalDate.parse(dateOfBirth, formatter);
        Period age = Period.between(birthdate, LocalDate.now());
        return "" + age.getYears();
    }

    private void setProfileImage() {
        if(pictureUrl == null) {
             setDefaultImage();
        } else {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imgRef = storageReference.child("images/" + pictureUrl);
            try {
                File localFile = File.createTempFile("tempFile", ".jpeg");
                imgRef.getFile(localFile).addOnSuccessListener(
                        taskSnapshot -> {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            binding.profileImage.setImageBitmap(bitmap);
                        }
                ).addOnFailureListener(
                        e -> Log.d("IMAGE DOWNLOAD","FAILED:" + e.getMessage())
                );
            } catch (Exception e) {
                Log.e("getImage", e.getMessage());
            }
        }
    }

    private void setDefaultImage() {
        binding.profileImage.setImageResource(R.drawable.default_user_image);
    }

    private void logout() {
        clearSharedPreference();
        navigateToEntryScreenActivity();
    }

    private void clearSharedPreference() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("onboarding", "seen");
        editor.apply();
    }

    private void navigateToEntryScreenActivity() {
        Intent intent = new Intent(getActivity(), EntryScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        getActivity().finish();
    }
}