package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ModifyMyPost extends AppCompatActivity {
    EditText postTitle;
    Button backToAll;
    EditText postPrice;
    EditText postDesc;
    EditText postAdd;
    EditText postZip;
    Spinner category;
    Button submitChange;
    Button deletePost;

    String title;
    String desc;
    String address;
    String zip;
    double price;
    final String TAG = "MODIFY POST";

    FirebaseAuth fAuth;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_my_post);
        //GET ALL
        postTitle = findViewById(R.id.editTextmModifyTitle);
        postPrice = findViewById(R.id.editTextModifyPrice);
        postDesc = findViewById(R.id.editTextModifyDes);
        category = findViewById(R.id.spinnerModifyCategory);
        backToAll = findViewById(R.id.btnBackToAllMyPosts);
        postZip = findViewById(R.id.editTextModifyZip);
        postAdd = findViewById(R.id.editTextModifyAddress);
        submitChange = findViewById(R.id.buttonSubmitModified);
        deletePost = findViewById(R.id.buttonDeletePost);
        fAuth = FirebaseAuth.getInstance();
        dbHelper = DatabaseHelper.getInstance(this);

        //back to all
        backToAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ModifyMyPost.this, ViewMyPosts.class));
            }
        });
        //fill in the field
        try{
            int postId = getIntent().getExtras().getInt("PostId");
            Post thisPost = dbHelper.getPostById(postId);
            User thisUser = dbHelper.getUserByEmail(fAuth.getCurrentUser().getEmail());

            postTitle.setText(thisPost.getTitle());
            postPrice.setText(String.valueOf(thisPost.getPrice()));
            postDesc.setText(thisPost.getDescription());
            postAdd.setText(thisPost.getAddress());
            postZip.setText(thisPost.getZipCode());

            //submit btn click
            submitChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String modifiedcat = category.getSelectedItem().toString();

                    thisPost.setPostId(postId);
                    thisPost.setTitle(postTitle.getText().toString());
                    thisPost.setPrice(Double.parseDouble(postPrice.getText().toString()));
                    thisPost.setDescription(postDesc.getText().toString());
                    thisPost.setAddress(postAdd.getText().toString());
                    thisPost.setZipCode(postZip.getText().toString());
                    thisPost.setCategory(modifiedcat);

                    dbHelper.updatePost(thisPost);

                    Post updatedPost = dbHelper.getPostById(thisPost.getPostId());

                    Toast.makeText(ModifyMyPost.this, updatedPost.getDescription() , Toast.LENGTH_LONG).show();
                    Toast.makeText(ModifyMyPost.this, thisPost.getDescription() , Toast.LENGTH_SHORT).show();
                    Intent goToPost = new Intent(ModifyMyPost.this, PostDetails.class);
                    //create Bundle
                    Bundle myBundle = new Bundle();
                    myBundle.putInt("PostId", thisPost.getPostId());
                    goToPost.putExtras(myBundle);
                    startActivity(goToPost);
                }
            });

            //delete
            deletePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        catch(Exception ex){

        }





    }
}