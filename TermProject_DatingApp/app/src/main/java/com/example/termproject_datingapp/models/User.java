package com.example.termproject_datingapp.models;

import java.util.Date;

public class User {
    String userId;
    String firstName;
    String lastName;
    String email;
    String emailVerificationStatus;
    String program;
    String gender;
    String bio;
    String profilePicUrl;
    Date dateOfBirth;


    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailVerificationStatus() {
        return emailVerificationStatus;
    }

    public String getProgram() {
        return program;
    }

    public String getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailVerificationStatus(String emailVerificationStatus) {
        this.emailVerificationStatus = emailVerificationStatus;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
