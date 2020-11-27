package com.example.seektradeapp;

public class Post {
    private int postId;
    private String category;
    private String title;
    private String description;
    private double price;
    private String userEmail;
    private String postDate;
    private String address;
    private String zipCode;
    private String photo;

    public Post(int postId, String category, String title, String description, double price, String userEmail, String postDate, String address, String zipCode, String photo) {
        this.postId = postId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.userEmail = userEmail;
        this.postDate = postDate;
        this.address = address;
        this.zipCode = zipCode;
        this.photo = photo;
    }
    public Post(String category, String title, String description, double price, String userEmail, String postDate, String address, String zipCode, String photo) {
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.userEmail = userEmail;
        this.postDate = postDate;
        this.address = address;
        this.zipCode = zipCode;
        this.photo = photo;
    }

    public Post() {
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;

    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getPhoto() {
        return photo;
    }


    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
