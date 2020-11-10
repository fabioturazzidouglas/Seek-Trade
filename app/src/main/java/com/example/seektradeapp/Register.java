package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

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

        newUserFullName = findViewById(R.id.newUserFullName);
        email = findViewById(R.id.textEmail);
        pass = findViewById(R.id.textPassword);
        phone = findViewById(R.id.textPhone);
        regBtn = findViewById(R.id.regBtn);
        goToLogin = findViewById(R.id.textViewToLogin);

        fAuth = FirebaseAuth.getInstance();
        progBar = findViewById(R.id.progressBar);
    }
}