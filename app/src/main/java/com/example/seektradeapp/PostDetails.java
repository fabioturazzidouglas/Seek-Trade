package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        TextView textViewPostTitle = findViewById(R.id.textViewPostTitle);
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        TextView textViewDetails = findViewById(R.id.textViewDetails);
        TextView textViewPrice = findViewById(R.id.textViewPrice);
        TextView textViewUser = findViewById(R.id.textViewUser);
        TextView textViewPostDate = findViewById(R.id.textViewPostDate);
        TextView textViewCategory = findViewById(R.id.textViewCategory);
        ImageView imageViewReturn = findViewById(R.id.imageViewReturn);

        try {
            int postId = getIntent().getExtras().getInt("PostId");
            Post thisPost = dbHelper.getPostById(postId);
            User thisUser = dbHelper.getUserById(thisPost.getUserId());


            textViewPostTitle.setText(thisPost.getTitle());
            textViewLocation.setText(thisPost.getAddress());
            textViewDetails.setText(thisPost.getDescription());
            textViewPrice.setText("$ " + thisPost.getPrice());
            textViewUser.setText(thisUser.getEmail());
            textViewPostDate.setText(thisPost.getPostDate());
            textViewCategory.setText(thisPost.getCategory());

        } catch(Exception ex) {
            Log.e("Post Details","Post not found " + ex.getMessage());
        }

        imageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain= new Intent(PostDetails.this, SearchPostsActivity.class);
                startActivity(goToMain);

            }
        });
    }
}