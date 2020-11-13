package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostPreview extends AppCompatActivity {
    final String TAG = "PREVIEW POST";
    String postTitle;
    String postDesc;
    double price;
    int cat_pos;
    String cat;
    String address;
    String zipcode;
    FirebaseAuth fAuth;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_preview);
        TextView textViewPostTitle = findViewById(R.id.textViewPreviewPostTitle);
        TextView textViewLocation = findViewById(R.id.textViewPreviewLocation);
        TextView textViewDetails = findViewById(R.id.textViewPreviewDetails);
        TextView textViewPrice = findViewById(R.id.textViewPreviewPrice);
        TextView textViewUser = findViewById(R.id.textViewPreviewUser);
        TextView textViewPostDate = findViewById(R.id.textViewPreviewPostDate2);
        TextView textViewCategory = findViewById(R.id.textViewPreviewCategory);
        ImageView imageViewPreviewPic = findViewById(R.id.imageViewPreview);
        Button backtoEdit = findViewById(R.id.buttonBackToEdit);
        Button publish = findViewById(R.id.buttonPublishPost);
        fAuth = FirebaseAuth.getInstance();
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd"), Locale.getDefault());
        String date = df.format(d);

        try {
            price = getIntent().getExtras().getDouble("PRICE");
            cat_pos = getIntent().getExtras().getInt("CAT_POS");
            cat = getIntent().getExtras().getString("CAT");
            postTitle = getIntent().getExtras().getString("TITLE");
            postDesc = getIntent().getExtras().getString("DESC");
            address = getIntent().getExtras().getString("ADD");
            zipcode = getIntent().getExtras().getString("ZIP");

            textViewPostDate.setText(date);
            textViewUser.setText(fAuth.getCurrentUser().getEmail());
            textViewPostTitle.setText(postTitle);
            textViewDetails.setText(postDesc);
            textViewPrice.setText("$" + price);
            textViewCategory.setText(cat);
            textViewLocation.setText(address + ", " + zipcode);

            //back to edit
            backtoEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent returnIntent = new Intent();
                    Bundle returnBundle = new Bundle();
                    returnBundle.putDouble("rprice", price);
                    returnBundle.putString("rcat", cat);
                    returnBundle.putString("rtitle", postTitle);
                    returnBundle.putString("rdes", postDesc);
                    returnIntent.putExtras(returnBundle);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    //startActivity(new Intent(PostPreview.this,CreatePostActivity.class));
                }
            });
            //publish post
            publish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dbHelper = DatabaseHelper.getInstance(PostPreview.this);

                    Log.e(TAG, "$" + price);

                    //create post
                    //User user = new User("Fabio", "10/10/2020", "fabio@email", "123");
                    Date d = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat(("yyyy-MM-dd"), Locale.getDefault());
                    String date = df.format(d);

//                    Set photo from FB
                    String photo = "photo";
                    //String category, String title, String description, double price, int userId, String postDate, String address, String zipCode, String[] photos) {
                    Post newPost = new Post(cat,postTitle, postDesc, price,  fAuth.getCurrentUser().getEmail(), date, address, zipcode,photo );


                    //Add Post to the database
                    dbHelper.addPost(newPost,  fAuth.getCurrentUser().getEmail());

                    Intent myIntent = new Intent(PostPreview.this, SearchPostsActivity.class);
                    Bundle myBundle = new Bundle();
                    myBundle.putString("title", postTitle);
                    myIntent.putExtras(myBundle);
                    startActivity(myIntent);

                }
            });

        } catch (Exception ex) {
            Log.e(TAG, ex.getMessage());
        }
    }
}