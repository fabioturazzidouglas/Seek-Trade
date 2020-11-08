package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Post> allActivePosts;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //look up recyclerview in main layout
        RecyclerView recyclerView_listing = (RecyclerView) findViewById(R.id.recyclerView_listings);
        //initialize posts
        allActivePosts = Post.createPostList(10);
        //create adapter, pass data
        PostAdapter myPostAdapter = new PostAdapter(allActivePosts);
        //attach adapter to recyclerview
        recyclerView_listing.setAdapter(myPostAdapter);
        //set layout manager to position the item
        recyclerView_listing.setLayoutManager(new LinearLayoutManager(this));

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