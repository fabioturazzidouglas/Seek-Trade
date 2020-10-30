package com.example.seektradeapp;

public class Photo {
    private int postId;
    private String imageLink;

    public Photo(int postId, String imageLink) {
        this.postId = postId;
        this.imageLink = imageLink;
    }

    public Photo() {
        this.postId = postId;
        this.imageLink = imageLink;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
