package com.example.seektradeapp;

import java.util.Date;

public class Post {
    private int postId;
    private String category;
    private String title;
    private String description;
    private double price;
    private int userId;
    private Date postDate;
    private String address;

    public Post(int postId, String category, String title, String description, double price, int userId, Date postDate, String address, String zipCode) {
        this.postId = postId;
        this.category = category;
        this.title = title;
        this.description = description;
        this.price = price;
        this.userId = userId;
        this.postDate = postDate;
        this.address = address;
        this.zipCode = zipCode;
    }

    public Post() {
    }

    private String zipCode;

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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
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
}
