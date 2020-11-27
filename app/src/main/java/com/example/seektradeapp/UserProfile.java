package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class UserProfile extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        TextView fullName, registrationDate, email;
        FirebaseAuth fAuth;
        Button logOutProfileBtn, viewPostsBtn, editProfileBtn, deleteProfileBtn;
        ImageView backButton;

        fAuth = FirebaseAuth.getInstance();

//        if(fAuth.getCurrentUser() == null) {
//            startActivity(new Intent(getApplicationContext(), Login.class));
//            finish();
//        }

        // get email of current user
        String userEmail = fAuth.getCurrentUser().getEmail();

        //Instantiate database helper
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        //initialize user
        User newUser = dbHelper.getUserByEmail(userEmail);

        email = findViewById(R.id.userEmailTxtView);
        email.setText(userEmail);

        registrationDate = findViewById(R.id.dateOfCreationTxtView);
        registrationDate.setText(newUser.getRegistrationDate());

        fullName = findViewById(R.id.fullNameProfileTxtView);
        fullName.setText(newUser.getFullName());

        logOutProfileBtn = findViewById(R.id.logOutBtnProfileView);
        viewPostsBtn = findViewById(R.id.viewMyPostsBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        deleteProfileBtn = findViewById(R.id.deleteProfileBtn);
        backButton = findViewById(R.id.imageViewReturn2);

        logOutProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
            }
        });

        viewPostsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewMyPosts.class));
            }
        });

        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeleteProfile.class));
            }
        });

        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditProfile.class));
            }
        });

    }
}