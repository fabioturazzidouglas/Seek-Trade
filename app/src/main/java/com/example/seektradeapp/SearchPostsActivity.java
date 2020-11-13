package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.List;

public class SearchPostsActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;

    final String TAG = "Search Posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_posts);
    }
    @Override
    protected void onResume() {
        super.onResume();

        //Toast.makeText(SearchPostsActivity.class, "Post created", Toast.LENGTH_LONG).show()\\
        try {
            String title ="";
                    getIntent().getExtras().getString("title");
            if (!title.equals("")) {
                Toast.makeText(SearchPostsActivity.this, "Post created: " + title, Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Log.e(TAG, "Cannot get newly created post");
        }

        //Instantiate database helper
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        //initialize posts
        List<Post> allActivePosts = dbHelper.getAllPosts();

        //look up list in main layout
        ListView listViewPosts = findViewById(R.id.listViewPosts);
        ImageView searchIcon = findViewById(R.id.imageViewSearchIcon);
        ImageView addIcon = findViewById(R.id.imageViewAddPost);
        EditText searchedTitle = findViewById(R.id.editTextSearch);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategories);
        //create adapter, pass data
        PostAdapter myPostAdapter = new PostAdapter(allActivePosts);
        //attach adapter to recyclerview
        listViewPosts.setAdapter(myPostAdapter);

        //search for post
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchedText = searchedTitle.getText().toString();
                String searchedCategory = spinnerCategory.getSelectedItem().toString();

                List<Post> searchPosts = dbHelper.getAllPosts();
                allActivePosts.clear();

                boolean firstFilter;
                boolean secondFilter;

                for (Post eachPost : searchPosts) {
                    firstFilter = false;
                    secondFilter = false;
                    //Log.e(TAG, "Error fetching post");
                    if (searchedText.equals("") || eachPost.getTitle().contains(searchedText)) {
                        firstFilter = true;
                    }
                    if (searchedCategory.equals("All Categories") || eachPost.getCategory().equals(searchedCategory)) {
                        secondFilter = true;
                    }
                    if (firstFilter && secondFilter) {
                        allActivePosts.add(eachPost);
                    }
                }
                //create adapter, pass data
                PostAdapter myPostAdapter = new PostAdapter(allActivePosts);
                //attach adapter to recyclerview
                listViewPosts.setAdapter(myPostAdapter);
                Toast.makeText(SearchPostsActivity.this, allActivePosts.size() + " posts found!", Toast.LENGTH_LONG).show();

            }
        });

        //add a new post
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchPostsActivity.this, CreatePostActivity.class));
            }
        });

        //view individual post
        listViewPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Post clickedPost = allActivePosts.get(i);

                Intent goToPost = new Intent(SearchPostsActivity.this, PostDetails.class);
                //create Bundle
                Bundle myBundle = new Bundle();
                myBundle.putInt("PostId", clickedPost.getPostId());
                goToPost.putExtras(myBundle);
                startActivity(goToPost);
            }
        });


    }
}