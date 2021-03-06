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
import java.util.Date;
import java.util.List;

public class Register extends AppCompatActivity {

    EditText newUserFullName, email, pass, confPass;
    Button regBtn;
    TextView goToLogin;
    FirebaseAuth fAuth;
    ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        newUserFullName = findViewById(R.id.newUserFullName);
        email = findViewById(R.id.textEmail);
        pass = findViewById(R.id.textPassword);
        confPass = findViewById(R.id.confirmPassword);
        regBtn = findViewById(R.id.regBtn);
        goToLogin = findViewById(R.id.textViewToLogin);

        fAuth = FirebaseAuth.getInstance();
        progBar = findViewById(R.id.progressBar);


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nEmail = email.getText().toString().trim();
                String password = pass.getText().toString();
                String confirmPass = confPass.getText().toString();

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

                if (TextUtils.isEmpty(confirmPass)) {
                    confPass.setError("Please, confirm your password.");
                    return;
                }

                if (confirmPass.compareTo(password) != 0) {
                    confPass.setError("Passwords do not match.");
                    return;
                }

                progBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(nEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Date today = new Date();
                            User newUser = new User(fAuth.getCurrentUser().getUid(), newUserFullName.getText().toString(), today.toString(), nEmail);
                            dbHelper.addOrUpdateUser(newUser);

                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), SearchPostsActivity.class));
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
}