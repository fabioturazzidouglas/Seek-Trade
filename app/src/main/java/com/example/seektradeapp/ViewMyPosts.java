package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ViewMyPosts extends AppCompatActivity {
    TextView user;
    ListView listViewallMyPosts;
    List<Post> allMyPosts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_posts);

        //get user id/email

        //populate posts from db

        //get items from layout
        user = findViewById(R.id.textViewUserName);
        listViewallMyPosts = findViewById(R.id.listViewListMyPosts);
        //set adapter
        MyPostAdapter myAdapter = new MyPostAdapter(allMyPosts);
        //attach adapter to recyclerview
        listViewallMyPosts.setAdapter(myAdapter);

        //choose a post to edit/delete
        listViewallMyPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post clickedPost = allMyPosts.get(position);

                Intent goToPost = new Intent(ViewMyPosts.this, ModifyMyPost.class);
                //create Bundle
                Bundle myBundle = new Bundle();
                myBundle.putInt("PostId", clickedPost.getPostId());
                goToPost.putExtras(myBundle);
                startActivity(goToPost);
            }
        });

    }
}