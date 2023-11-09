package com.example.termproject_datingapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static void isValidName(String firstName, String lastName) throws Exception {
        if(firstName.isEmpty() || lastName.isEmpty()) throw new Exception("Enter your name");
    };

    public static void isValidEmailFormat(String email) throws Exception {
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
    };

    public static void isValidProgramSelected(String program) throws Exception {
        if(program.isEmpty()) {
            throw new Exception("Program is not selected");
        };
    };

    public static void isValidPassword(String password, String confirmPassword) throws Exception {
        isSecurePassword(password);
        if(!password.equals(confirmPassword)) throw new Exception("Password doesn't match");
    }

    private static void isSecurePassword(String password) throws Exception {
        // include at least one upper case, lower case, digit and special character and more at least 8 characters long
        String strongPasswordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()-_+=<>?]).{8,}$";
        Pattern pattern = Pattern.compile(strongPasswordPattern);
        Matcher matcher = pattern.matcher(password);
        if(!matcher.matches()) throw new Exception("Password must include a mix of uppercase and lowercase letters, at least one digit, one special character (e.g., !@#$%^&*), and be at least 8 characters long.");
    }

    public static void validateSignInInput(String email, String password) throws Exception {
        if(email.isEmpty() || password.isEmpty()) {
            throw new Exception("Type your email and password");
        }
    }
}
