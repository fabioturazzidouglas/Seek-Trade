package com.example.seektradeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        ImageView imageViewPhoto = findViewById(R.id.imageViewPost);
<<<<<<< HEAD
        Button returnBtn = findViewById(R.id.buttonReturn);
=======
        Button btnContactSeller = findViewById(R.id.btnContactUser);
>>>>>>> sendEmail_FEATURE
        fAuth = FirebaseAuth.getInstance();
        //Get instances for FirebaseStorage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Navbar objects and onclicklisteners:
        ImageView addIcon = findViewById(R.id.imageViewAddPost);
        ImageView toMyPosts = findViewById(R.id.imageViewToMyPosts);
        Button logOut = findViewById(R.id.logoutBtn);
        ImageView toSearch = findViewById(R.id.imageViewSearchPosts);

        //Event to go to search
        toSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostDetails.this, SearchPostsActivity.class));
            }
        });


        //event for logout button
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(PostDetails.this, Login.class));
                finish();
            }
        });

        //add a new post
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostDetails.this, CreatePostActivity.class));
            }
        });

        toMyPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ViewMyPosts.class));

            }
        });

        returnBtn.setOnClickListener(new View.OnClickListener() {
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
        btnContactSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");

                int postId = getIntent().getExtras().getInt("PostId");
                Post thisPost = dbHelper.getPostById(postId);

                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Seek & Trade: " + thisPost.getTitle());
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{thisPost.getUserEmail()});
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(PostDetails.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
            });




    }
}