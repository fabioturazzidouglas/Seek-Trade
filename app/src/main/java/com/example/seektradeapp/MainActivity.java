package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = new User("Fabio", "10/10/2020", "fabio@email", "123");
        TextView txtViewTitle = findViewById(R.id.textView);


        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        dbHelper.resetDB();


        dbHelper.addOrUpdateUser(user);

        User printUser = dbHelper.getAllUsers().get(0);

        String[] photos = {"1","2","3"};
        String[] photos2 = {"a","b"};
        Post post = new Post("Cat", "Title", "Descr", 2, printUser.getUserId(),"10/10/2020","add","zip", photos2);

        dbHelper.addPost(post, printUser);

        Post printPost = dbHelper.getAllPosts().get(0);

        dbHelper.addPhotos(printPost, photos);

        Post searchPost = dbHelper.getPostById(printPost.getPostId());

        txtViewTitle.setText(Arrays.toString(searchPost.getPhotos()) + ", " + searchPost.getTitle());


//        String[] printPics = dbHelper.getPhotosByPostId(printPost.getPostId());
//
//        txtViewTitle.setText(Arrays.toString(printPics));

    }
}