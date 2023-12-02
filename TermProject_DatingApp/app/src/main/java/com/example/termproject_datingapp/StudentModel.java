package com.example.termproject_datingapp;

public class StudentModel {

    private  int image;
    private String studentName;
    private String studentMajor;
    private String studentAge;
    private String studentCity;

    public StudentModel(){

    }

    public StudentModel(int image, String name, String major, String age, String city){
        this.image = image;
        this.studentName = name;
        this.studentMajor = major;
        this.studentAge = age;
        this.studentCity = city;
    }


    public String getStudentAge() {
        return studentAge;
    }

    public void setStudentAge(String studentAge) {
        this.studentAge = studentAge;
    }

    public String getStudentCity() {
        return studentCity;
    }

    public void setStudentCity(String studentCity) {
        this.studentCity = studentCity;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentMajor() {
        return studentMajor;
    }

    public void setStudentMajor(String studentMajor) {
        this.studentMajor = studentMajor;
    }
}
