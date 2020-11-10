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
}