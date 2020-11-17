package com.example.seektradeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostDetails extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
        DatabaseHelper dbHelper = DatabaseHelper.getInstance(this);
        TextView textViewPostTitle = findViewById(R.id.textViewPostTitle);
        TextView textViewLocation = findViewById(R.id.textViewLocation);
        TextView textViewDetails = findViewById(R.id.textViewDetails);
        TextView textViewPrice = findViewById(R.id.textViewPrice);
        TextView textViewUser = findViewById(R.id.textViewUser);
        TextView textViewPostDate = findViewById(R.id.textViewPostDate);
        TextView textViewCategory = findViewById(R.id.textViewCategory);
        ImageView imageViewReturn = findViewById(R.id.imageViewReturn);
        ImageView imageViewPhoto = findViewById(R.id.imageViewPost);
        fAuth = FirebaseAuth.getInstance();
        //Get instances for FirebaseStorage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        imageViewReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToMain= new Intent(PostDetails.this, SearchPostsActivity.class);
                startActivity(goToMain);

            }
        });

        try {
            int postId = getIntent().getExtras().getInt("PostId");
            Post thisPost = dbHelper.getPostById(postId);
            User thisUser = dbHelper.getUserByEmail(fAuth.getCurrentUser().getEmail());


            textViewPostTitle.setText(thisPost.getTitle());
            textViewLocation.setText(thisPost.getAddress());
            textViewDetails.setText(thisPost.getDescription());
            textViewPrice.setText("$ " + thisPost.getPrice());
            textViewUser.setText(thisUser.getEmail());
            textViewPostDate.setText(thisPost.getPostDate());
            textViewCategory.setText(thisPost.getCategory());
            StorageReference ref = storageReference.child(thisPost.getPhoto());

            Glide.with(PostDetails.this).clear(imageViewPhoto);

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(PostDetails.this)
                            .load(ref)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imageViewPhoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });


        } catch(Exception ex) {
            Log.e("Post Details","Post not found " + ex.getMessage());
        }

    }
}