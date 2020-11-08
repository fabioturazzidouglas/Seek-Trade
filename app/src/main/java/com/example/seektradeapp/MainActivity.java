package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;


import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.List;

public class MainActivity extends AppCompatActivity {
RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Post> allActivePosts = new ArrayList<Post>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //look up recyclerview in main layout
        RecyclerView recyclerView_listing = (RecyclerView) findViewById(R.id.recyclerView_listings);

        User user = new User("Fabio", "10/10/2020", "fabio@email", "123");
        TextView txtViewTitle = findViewById(R.id.textView);


        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        dbHelper.resetDB();


        dbHelper.addOrUpdateUser(user);

        User printUser = dbHelper.getAllUsers().get(0);

        String[] photos = {"1","2","3"};
        String[] photos2 = {"a","b"};
        //initialize posts
        Post post1 = new Post("Cat", "Title1", "Descr", 2, printUser.getUserId(),"10/10/2020","add","zip", photos2);
        Post post2 = new Post("Cat1", "Title2", "Descr1", 20, printUser.getUserId(),"11/11/2010","add","zip", photos2);
        Post post3 = new Post("Cat2", "Title3", "Descr", 50, printUser.getUserId(),"10/10/2020","add","zip", photos2);
        Post post4 = new Post("Cat3", "Title4", "Descr1", 200, printUser.getUserId(),"11/11/2010","add","zip", photos2);
        Post post5 = new Post("Cat", "Title5", "Descr", 200, printUser.getUserId(),"10/10/2020","add","zip", photos2);
        Post post6 = new Post("Cat1", "Title6", "Descr1", 290, printUser.getUserId(),"11/11/2010","add","zip", photos2);
        Post post7 = new Post("Cat2", "Title7", "Descr", 501, printUser.getUserId(),"10/10/2020","add","zip", photos2);
        Post post8 = new Post("Cat3", "Title8", "Descr1", 2020, printUser.getUserId(),"11/11/2010","add","zip", photos2);
        allActivePosts.add(post1);
        allActivePosts.add(post2);
        allActivePosts.add(post3);
        allActivePosts.add(post4);
        allActivePosts.add(post5);
        allActivePosts.add(post6);
        allActivePosts.add(post7);
        allActivePosts.add(post8);

        //create adapter, pass data
        PostAdapter myPostAdapter = new PostAdapter(allActivePosts);

        //attach adapter to recyclerview
        recyclerView_listing.setAdapter(myPostAdapter);
        //
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        //set layout manager to position the item
        //recyclerView_listing.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_listing.setLayoutManager(layoutManager);

        //to add new data to existing list:
        //allActivePosts.addAll();
        /*
        Unlike ListView, there is no way to add or remove items directly through the RecyclerView adapter.
        => need to make changes to the data source directly and notify the adapter of any changes.
        Also, whenever adding or removing elements, always make changes to the existing list.
         */
        //update and notify changes
        //int curSize = myPostAdapter.getItemCount();
        //put the code to generate all listing from database here
       // List<Post> newItems = null;
        //update existing list
       // allActivePosts.addAll(newItems);
        // curSize should represent the first element that got added
        // newItems.size() represents the itemCount
        //myPostAdapter.notifyItemRangeChanged(curSize, newItems.size());

        //sort existing list: call adapter.notifyDataSetChanged()

    }
}