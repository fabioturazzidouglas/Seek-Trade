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

        populateDatabase();

        mEmail = findViewById(R.id.textEmailLogIn);
        mPass = findViewById(R.id.textPasswordLogIn);
        loginBtn = findViewById(R.id.logInBtn);
        goToReg = findViewById(R.id.textViewToRegister);

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

        User user1 = new User("Jabbah", "10/10/2020", "iluvleya@tatooinemail.com", "xxxxxx");
        User user2 = new User("fabfap", "10/10/2020", "fabfap@gmail.com", "xxxxxx");
        User user3 = new User("Admin", "10/10/2020", "admin@admin.com", "xxxxxx");
        User user4 = new User("Guyn", "10/10/2020", "guyn@hotmail.com", "xxxxxx");
        User user5 = new User("Daniil", "10/10/2020", "denngall2@gmail.com", "xxxxxx");
        User user6 = new User("Uyen", "10/10/2020", "uyen@gmail.com", "xxxxxx");

        allActiveUsers.add(user1);
        allActiveUsers.add(user2);
        allActiveUsers.add(user3);
        allActiveUsers.add(user4);
        allActiveUsers.add(user5);
        allActiveUsers.add(user6);

        for(User user : allActiveUsers) {
            dbHelper.addOrUpdateUser(user);
        }

        Post post1 = new Post("Real Estate", "Title1", "Descr", 2, user1.getEmail(), "10/10/2020", "add", "zip", "photo");
        Post post2 = new Post("Academic", "Title2", "Descr1", 20, user2.getEmail(), "11/11/2010", "add", "zip", "photo");
        Post post3 = new Post("Academic", "Title3", "Descr", 50, user3.getEmail(), "10/10/2020", "add", "zip", "photo");
        Post post4 = new Post("Vehicles", "Title4", "Descr1", 200, user4.getEmail(), "11/11/2010", "add", "zip", "photo");
        Post post5 = new Post("Services", "Title5", "Descr", 200, user5.getEmail(), "10/10/2020", "add", "zip", "photo");
        Post post6 = new Post("Housing Rental", "Title6", "Descr1", 290, user1.getEmail(), "11/11/2010", "add", "zip", "photo");
        Post post7 = new Post("Electronics", "Title7", "Descr", 501, user2.getEmail(), "10/10/2020", "add", "zip", "photo");
        Post posttest = new Post("Electronics", "Title7t", "Descr", 501, user3.getEmail(), "10/10/2020", "add", "zip", "photo");
        Post post8 = new Post("Music, Films", "Title8", "Descr1", 2020, user4.getEmail(), "11/11/2010", "add", "zip", "photo");
        allActivePosts.add(post1);
        allActivePosts.add(post2);
        allActivePosts.add(post3);
        allActivePosts.add(post4);
        allActivePosts.add(post5);
        allActivePosts.add(post6);
        allActivePosts.add(post7);
        allActivePosts.add(post8);
        allActivePosts.add(posttest);

        for (Post post : allActivePosts) {
            dbHelper.addPost(post, post.getUserEmail());
        }


    }
}