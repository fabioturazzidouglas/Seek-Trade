package com.example.seektradeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;

    final String TAG = "Search Posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Populate posts
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        populatePostDatabase();
        //initialize posts
        List<Post> allActivePosts = dbHelper.getAllPosts();

        //look up recyclerview in main layout
        ListView listViewPosts = findViewById(R.id.listViewPosts);
        ImageView searchIcon = findViewById(R.id.imageViewSearchIcon);
        EditText searchedTitle = findViewById(R.id.editTextSearch);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategories);
        //create adapter, pass data
        PostAdapter myPostAdapter = new PostAdapter(allActivePosts);
        //attach adapter to recyclerview
        listViewPosts.setAdapter(myPostAdapter);


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
                    if(searchedText.equals("") || eachPost.getTitle().contains(searchedText)) {
                        firstFilter = true;
                    }
                    if(searchedCategory.equals("All Categories") || eachPost.getCategory().equals(searchedCategory)) {
                        secondFilter = true;
                    }
                    if(firstFilter && secondFilter) {
                        allActivePosts.add(eachPost);
                    }
                }
                //create adapter, pass data
                PostAdapter myPostAdapter = new PostAdapter(allActivePosts);
                //attach adapter to recyclerview
                listViewPosts.setAdapter(myPostAdapter);
                Toast.makeText(MainActivity.this, allActivePosts.size() + " posts found!", Toast.LENGTH_LONG).show();

            }
        });

        listViewPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Post clickedPost = allActivePosts.get(i);

                Intent goToPost = new Intent(MainActivity.this, PostDetails.class);

                //create Bundle
                Bundle myBundle =new Bundle();
                myBundle.putInt("PostId", clickedPost.getPostId());

                goToPost.putExtras(myBundle);
                startActivity(goToPost);
            }
        });


    }

    public void populatePostDatabase() {

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.resetDB();

        List<Post> allActivePosts = new ArrayList<Post>();


        String[] photos = {"1", "2", "3"};
        String[] photos2 = {"a", "b"};

        User user = new User("Fabio", "10/10/2020", "fabio@email", "123");

        dbHelper.addOrUpdateUser(user);

        User addedUser = dbHelper.getAllUsers().get(0);

        Post post1 = new Post("Real Estate", "Title1", "Descr", 2, addedUser.getUserId(), "10/10/2020", "add", "zip", photos2);
        Post post2 = new Post("Academic", "Title2", "Descr1", 20, addedUser.getUserId(), "11/11/2010", "add", "zip", photos2);
        Post post3 = new Post("Academic", "Title3", "Descr", 50, addedUser.getUserId(), "10/10/2020", "add", "zip", photos2);
        Post post4 = new Post("Vehicles", "Title4", "Descr1", 200, addedUser.getUserId(), "11/11/2010", "add", "zip", photos2);
        Post post5 = new Post("Services", "Title5", "Descr", 200, addedUser.getUserId(), "10/10/2020", "add", "zip", photos2);
        Post post6 = new Post("Housing Rental", "Title6", "Descr1", 290, addedUser.getUserId(), "11/11/2010", "add", "zip", photos2);
        Post post7 = new Post("Electronics", "Title7", "Descr", 501, addedUser.getUserId(), "10/10/2020", "add", "zip", photos2);
        Post post8 = new Post("Music, Films", "Title8", "Descr1", 2020, addedUser.getUserId(), "11/11/2010", "add", "zip", photos2);
        allActivePosts.add(post1);
        allActivePosts.add(post2);
        allActivePosts.add(post3);
        allActivePosts.add(post4);
        allActivePosts.add(post5);
        allActivePosts.add(post6);
        allActivePosts.add(post7);
        allActivePosts.add(post8);

        for (Post post : allActivePosts) {
            dbHelper.addPost(post, addedUser);
//            dbHelper.addPhotos(post, photos);
        }


    }
}