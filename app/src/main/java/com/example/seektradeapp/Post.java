package com.example.seektradeapp;

import java.util.ArrayList;

public class Post {
    private int postid;
    private String postDate;

    //constructor
    public Post(int id, String date){
        postid = id;
        postDate = date;
    }

    //getter
    public int getPostid() {
        return postid;
    }

    public String getPostDate() {
        return postDate;
    }

    public static int lastPostId =0;

    //change this method
    public static ArrayList<Post> createPostList(int numOfPosts){
        ArrayList<Post> allPosts = new ArrayList<Post>();
        for (int i = 1; i <= numOfPosts; i++) {
            allPosts.add(new Post(++lastPostId, ""));
        }
        return allPosts;
    }

}
