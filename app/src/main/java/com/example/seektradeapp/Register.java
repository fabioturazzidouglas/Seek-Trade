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

public class Register extends AppCompatActivity {

    EditText newUserFullName, email, pass, phone;
    Button regBtn;
    TextView goToLogin;
    FirebaseAuth fAuth;
    ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        populatePostDatabase();

        newUserFullName = findViewById(R.id.newUserFullName);
        email = findViewById(R.id.textEmail);
        pass = findViewById(R.id.textPassword);
        phone = findViewById(R.id.textPhone);
        regBtn = findViewById(R.id.regBtn);
        goToLogin = findViewById(R.id.textViewToLogin);

        fAuth = FirebaseAuth.getInstance();
        progBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nEmail = email.getText().toString().trim();
                String password = pass.getText().toString();

                if(TextUtils.isEmpty(nEmail)) {
                    email.setError("Email is Required.");
                    return;
                }

                if(TextUtils.isEmpty(password)) {
                    pass.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6) {
                    pass.setError("Password Must have more than 6 Characters");
                }

                progBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(nEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
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
        Post posttest = new Post("Electronics", "Title7t", "Descr", 501, addedUser.getUserId(), "10/10/2020", "add", "zip", photos2);
        Post post8 = new Post("Music, Films", "Title8", "Descr1", 2020, addedUser.getUserId(), "11/11/2010", "add", "zip", photos2);
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
            dbHelper.addPost(post, addedUser);
//            dbHelper.addPhotos(post, photos);
        }


    }
}