package com.example.termproject_datingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import java.util.Map;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private String updateState, userId, firstName, lastName, program, gender, bio, pictureUrl, dateOfBirth;
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
        binding.editBuutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(ProfileFragment.this).navigate();
            }
        });
    }

    private void getDataFromSharedPref() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("config_file", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","null");
        firstName = sharedPreferences.getString("firstName","null");
        lastName = sharedPreferences.getString("lastName","null");
        program = sharedPreferences.getString("program","null");
        gender = sharedPreferences.getString("gender","null");
        pictureUrl = sharedPreferences.getString("pictureUrl","null");
        dateOfBirth = sharedPreferences.getString("dateOfBirth","null");
        bio = sharedPreferences.getString("bio","User has not written BIO yet");
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
                    pictureUrl = profileMap.get("pictureUrl");
                    dateOfBirth = profileMap.get("dateOfBirth");
                    bio = profileMap.get("bio");
                }

                Profile profileModel = createProfileModel();
                saveProfileToSharedPreferences(profileModel);
                setProfileDataToView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("onCancelled", "Retrieving data cancelled");
            }
        });
    }

    private Profile createProfileModel() {
        Profile profile = new Profile();
        profile.setUserId(userId);
        profile.setGender(gender);
        profile.setProfilePicUrl(pictureUrl);
        profile.setDateOfBirth(dateOfBirth);
        profile.setBio(bio);

        return profile;
    }

    private void saveProfileToSharedPreferences(Profile profile) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("config_file", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("gender", profile.getGender());
        editor.putString("pictureUrl", profile.getProfilePicUrl());
        editor.putString("dateOfBirth", profile.getDateOfBirth());
        editor.putString("bio", profile.getBio());

        editor.apply();
    }

    private void setProfileDataToView() {
        binding.profileName.setText(firstName + " " + lastName);
        setGenderAndAge();
        setProfileImage();
        binding.program.setText(program);
        binding.bio.setText(bio);

    }

    private void setGenderAndAge() {
        String age = calcAge();

        if(gender.equals("null")) {
            gender = "?";
            binding.GenderAndAge.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gender_hidden)));
        } else if(gender.equals("M")) {
            binding.GenderAndAge.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gender_man)));
        } else if(gender.equals("W")) {
            binding.GenderAndAge.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.gender_woman)));
        }
        binding.GenderAndAge.setText(gender + " " + age);
    }
    private String calcAge() {
        String age = "??";
        if(dateOfBirth.equals("null")) {
            return age;
        }

        // somehow calc
        return age;
    }

    private void setProfileImage() {
        if(pictureUrl.equals("null")) {
             setDefaultImage();
        } else {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imgRef = storageReference.child("images/" + pictureUrl);
            try {
                File localFile = File.createTempFile("tempFile", ".jpeg");
                imgRef.getFile(localFile).addOnSuccessListener(
                        new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                                binding.profileImage.setImageBitmap(bitmap);
                            }
                        }
                ).addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "read file failed try again", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            } catch (Exception e) {
                Log.e("getImage", e.getMessage());
            }
        }
    }

    private void setDefaultImage() {
        binding.profileImage.setImageResource(R.drawable.default_user_image);
    }
}