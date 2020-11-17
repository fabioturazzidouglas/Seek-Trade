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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.List;

public class ViewMyPosts extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_posts);

        TextView txtViewFullName = findViewById(R.id.textViewUserName);
        ListView listViewAllMyPosts = findViewById(R.id.listViewListMyPosts);;
        FirebaseAuth fAuth = FirebaseAuth.getInstance();;
        //get user id/email
        String userEmail = fAuth.getCurrentUser().getEmail();
        //Instantiate database helper
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        //initialize user
        User currUser = dbHelper.getUserByEmail(userEmail);

        //set full name
        String fullName = currUser.getFullName();
        txtViewFullName.setText(fullName);

        //populate posts from db
        List<Post> allMyPosts = dbHelper.getPostsByUserEmail(userEmail);
        //if user postes something
        if (allMyPosts.size()!=0) {
            //set adapter
            MyPostAdapter myAdapter = new MyPostAdapter(allMyPosts, ViewMyPosts.this);
            //attach adapter to listview
            listViewAllMyPosts.setAdapter(myAdapter);

        }
        //if user has no post
        else{
            TextView nopost = findViewById(R.id.textViewNoPost);
            nopost.setVisibility(View.VISIBLE);
        }
        //choose a post to edit/delete
        listViewAllMyPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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