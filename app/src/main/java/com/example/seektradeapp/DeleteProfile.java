package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DeleteProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        TextView message;
        FirebaseAuth fAuth;
        Button deleteProfileBtn, discardBtn;

        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        fAuth = FirebaseAuth.getInstance();
        message = findViewById(R.id.deleteMessageTextView);
        deleteProfileBtn = findViewById(R.id.confirmDeleteBtn);
        discardBtn = findViewById(R.id.discardBtn);

        String uEmail = fAuth.getCurrentUser().getEmail();

        String deleteMsg = "Do you want to delete " + uEmail + " profile?";
        message.setText(deleteMsg);

        deleteProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                dbHelper.deleteUserWithEmail(uEmail);
                Toast.makeText(DeleteProfile.this, "Profile successfully deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        discardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserProfile.class));
            }
        });

    }
}