package com.example.seektradeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditProfile extends AppCompatActivity {

    Button saveChanges;
    CheckBox changePassChkBx;
    TextView newUserFullName;
    EditText newFullName, oldPass, newPass, confNewPass;
    ProgressBar progBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        newUserFullName = findViewById(R.id.newUserFullName);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);

        changePassChkBx = findViewById(R.id.resetPasswordChcBx);

        FirebaseAuth fAuth = FirebaseAuth.getInstance();;
        //get user id/email
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = fAuth.getCurrentUser().getEmail();
        //initialize user
        User currUser = dbHelper.getUserByEmail(userEmail);

        //set full name
        String fullName = currUser.getFullName();
        newUserFullName.setText(fullName);

        newFullName = findViewById(R.id.newUserFullName);
        oldPass = findViewById(R.id.textOldPassword);
        newPass = findViewById(R.id.newPassword);
        confNewPass = findViewById(R.id.confirmNewPassword);

        saveChanges = findViewById(R.id.saveChangesBtn);

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nNewFullName = newFullName.getText().toString().trim();

                if(TextUtils.isEmpty(nNewFullName)) {
                    newFullName.setError("This Field is Required.");
                    return;
                } else {
                    currUser.setFullName(nNewFullName);
                }

                if (changePassChkBx.isChecked()) {

                    String oldPassword = oldPass.getText().toString();
                    String newPassword = newPass.getText().toString();
                    String confirmNewPassword = confNewPass.getText().toString();

                    if(TextUtils.isEmpty(oldPassword)) {
                        oldPass.setError("Password is Required.");
                        return;
                    }

                    fAuth.signInWithEmailAndPassword(userEmail, oldPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                oldPass.setError("Old Password is incorrect");
                                return;
                            } else {

                                boolean hasError = false;

                                if(TextUtils.isEmpty(newPassword)) {
                                    oldPass.setError("New Password is Required.");
                                    hasError = true;
                                    return;
                                }

                                if(newPassword.length() < 6) {
                                    hasError = true;
                                    newPass.setError("Password Must have more than 6 Characters");
                                    return;
                                }

                                if (TextUtils.isEmpty(confirmNewPassword)) {
                                    confNewPass.setError("Please, confirm your password.");
                                    hasError = true;
                                    return;
                                }

                                if (confirmNewPassword.compareTo(newPassword) != 0) {
                                    confNewPass.setError("Passwords do not match.");
                                    hasError = true;
                                    return;
                                }

                                if (!hasError) {
                                    user.updatePassword(confirmNewPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(EditProfile.this, "Password Updated-", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(EditProfile.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }

                dbHelper.addOrUpdateUser(currUser);
            }
        });


    }
}