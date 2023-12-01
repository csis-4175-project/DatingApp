package com.example.termproject_datingapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.*;
import android.widget.RadioButton;

import com.example.termproject_datingapp.databinding.FragmentEditProfileBinding;
import com.example.termproject_datingapp.models.GlobalAuth;
import com.example.termproject_datingapp.models.Profile;
import com.example.termproject_datingapp.utils.Validation;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.*;

import java.util.UUID;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private FirebaseUser firebaseUser;
    Uri imgUri;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseUser = GlobalAuth.getFirebaseUser();

        activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    imgUri = data.getData();
                    binding.profileImage2.setImageURI(imgUri);
                }
            }
        );
        binding.choosePicBtn.setOnClickListener(v -> choosePicture());
        binding.editSubmit.setOnClickListener(v -> submit());
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activityResultLauncher.launch(intent);
    }

    private void submit() {
        try{
            Profile profile = new Profile();
            profile.setUserId(firebaseUser.getUid());
            if(imgUri != null) {
                String uuid = UUID.randomUUID().toString();
                profile.setProfilePicUrl(uuid);
                firebaseStorage = FirebaseStorage.getInstance();
                storageReference = firebaseStorage.getReference();
                uploadImage(imgUri, uuid);
            }
            profile.setGender(getSelectedGender());
            profile.setDateOfBirth(Validation.returnDateStringIfValidated(binding.dobTextField.getText().toString()));
            profile.setBio(binding.bioTextField.getText().toString());

            saveProfileDataToDB(profile);
        } catch (Exception e) {
            Log.d("ERROR", "invalid input" + e.getMessage());
        }
    }

    private void uploadImage(Uri imgUri, String imgName) {
        StorageReference imgRef = storageReference.child("images/" + imgName);
        imgRef.putFile(imgUri).addOnSuccessListener(
                taskSnapshot -> Log.d("IMAGE UPLOAD","SUCCESS")
        ).addOnFailureListener(
                e -> Log.d("IMAGE UPLOAD","FAILED")
        );
    }

    private String getSelectedGender() {
        RadioButton radioButton = binding.getRoot().findViewById(binding.radioGroup.getCheckedRadioButtonId());
        String selectedGender = radioButton.getText().toString();
        if(selectedGender.equals("Man")) return "M";
        return "W";
    }

    private void saveProfileDataToDB(Profile profile) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Profile");
        databaseReference.child(profile.getUserId()).setValue(profile).addOnCompleteListener(savingData -> {
            if(savingData.isSuccessful()) {
                Log.d("saveUserToDB","successfully saved");
            } else {
                Log.d("saveUserToDB","process failed");
            }
            ((FragmentActivity) getContext())
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame_layout, new ProfileFragment()).addToBackStack(null)
                .commit();
        });
    }
}