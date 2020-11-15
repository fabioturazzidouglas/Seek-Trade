package com.example.seektradeapp;


public class User {
    private String userId;
    private String fullName;
    private String registrationDate;
    private String email;

    public User(String userId, String fullName, String registrationDate, String email) {
        this.userId = userId;
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.email = email;
    }

    public User(String fullName, String registrationDate, String email) {
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.email = email;
    }

    public User() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
