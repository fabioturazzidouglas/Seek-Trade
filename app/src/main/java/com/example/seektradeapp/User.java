package com.example.seektradeapp;


public class User {
    private String userId;
    private String fullName;
    private String registrationDate;
    private String email;
    private String password;

    public User(String userId, String fullName, String registrationDate, String email, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.email = email;
        this.password = password;
    }

    public User(String fullName, String registrationDate, String email, String password) {
        this.fullName = fullName;
        this.registrationDate = registrationDate;
        this.email = email;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
