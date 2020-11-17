package com.example.seektradeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText mEmail, mPass;
    Button loginBtn;
    TextView goToReg;
    FirebaseAuth fAuth;
    ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //populateDatabase();

        mEmail = findViewById(R.id.textEmailLogIn);
        mPass = findViewById(R.id.textPasswordLogIn);
        loginBtn = findViewById(R.id.logInBtn);
        goToReg = findViewById(R.id.textViewToRegister);
//        getActionBar().setIcon(R.drawable.logo);
//        getActionBar().setTitle("Seek&Trade App");

        fAuth = FirebaseAuth.getInstance();
        progBar = findViewById(R.id.progressBar2);

//        if(fAuth.getCurrentUser() != null) {
//            startActivity(new Intent(getApplicationContext(), SearchPostsActivity.class));
//            finish();
//        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String password = mPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPass.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    mPass.setError("Password Must have more than 6 Characters");
                }

                progBar.setVisibility(View.VISIBLE);

                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SearchPostsActivity.class));
                        } else {
                            Toast.makeText(Login.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        goToReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });
    }

    //Method to initially populate database (called once when setting up the app for demonstration purposes
    public void populateDatabase() {

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        dbHelper.resetDB();

        List<Post> allActivePosts = new ArrayList<Post>();
        List<User> allActiveUsers = new ArrayList<User>();

        User user1 = new User("xsfsf","Peter", "10/10/2020", "iluvleya@tatooinemail.com");
        User user2 = new User("xsfsf421", "fabfap", "10/10/2020", "fabfap@gmail.com");
        User user3 = new User("fadfa32","Admin", "10/10/2020", "admin@admin.com");
        User user4 = new User("weryr3","Guyn", "10/10/2020", "guyn@hotmail.com");
        User user5 = new User("fafey5w4","Daniil", "10/10/2020", "denngall2@gmail.com");
        User user6 = new User("wrqrqw3","Uyen", "10/10/2020", "uyen@gmail.com");

        allActiveUsers.add(user1);
        allActiveUsers.add(user2);
        allActiveUsers.add(user3);
        allActiveUsers.add(user4);
        allActiveUsers.add(user5);
        allActiveUsers.add(user6);

        for(User user : allActiveUsers) {
            dbHelper.addOrUpdateUser(user);
        }

        Post post1 = new Post("All Categories", "Brand new Bycicle", "Brand new Bycicle, used for a month and decided to sell because I got another one.", 200, user1.getEmail(), "10/10/2020", "Vancouver, BC", "FAS ASD", "images/bike.jpg");
        Post post2 = new Post("Vehicles", "Hyundai 2018", "Hyundai in good condition, from 2018. 10000 miles ran.", 15000, user2.getEmail(), "11/11/2010", "New West, BC", "ASD 142", "images/car.jpg");
        Post post3 = new Post("Real Estate", "House in New Westminster", "1000 sq feet comfortable house with 3 bedroms, 4 bathrooms and backyard.", 1500000, user3.getEmail(), "10/10/2020", "New West, BC", "V24 AS2", "images/house.jpg");
        Post post4 = new Post("Electronics", "Gaming PC", "With GTX 2070, 16GB RAM, 1TB SSD", 700, user4.getEmail(), "11/11/2010", "Surrey, BC", "AS3 2D2", "images/PC.jpg");
        Post post5 = new Post("All Categories", "Used Bycicle in good condition", "Used Bycicle in perfect condition.", 100, user5.getEmail(), "10/10/2020", "Vancouver, BC", "FAS ASD", "images/bike.jpg");
        Post post6 = new Post("Electronics", "PS5 - Unique Edition", "PS5, new release by Sony. Comes with one Dualsense controller ", 600, user6.getEmail(), "10/10/2020", "Surrey, BC", "XSA VAS", "images/Ps5.jpg");
        Post post7 = new Post("Electronics", "New PS5 with controllers and one game", "PS5, new release by Sony. Comes with one Dualsense controller and the new Horizon Zero Dawn 2", 501, user2.getEmail(), "10/10/2020", "Surrey, BC", "XSA VAS", "images/Ps5.jpg");
        Post post8 = new Post("Electronics", "Used Gaming PC - 2018", "With GTX 1070, 8GB RAM, 1TB SSD", 400, user4.getEmail(), "11/11/2010", "Surrey, BC", "AS3 2D2", "images/PC.jpg");
        allActivePosts.add(post1);
        allActivePosts.add(post2);
        allActivePosts.add(post3);
        allActivePosts.add(post4);
        allActivePosts.add(post5);
        allActivePosts.add(post6);
        allActivePosts.add(post7);
        allActivePosts.add(post8);

        for (Post post : allActivePosts) {
            dbHelper.addPost(post, post.getUserEmail());
        }
    }
}