package com.example.seektradeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class CreatePostActivity extends AppCompatActivity {
    int LAUNCH_SECOND_ACTIVITY = 1;
    EditText postTitle;
    Button cancelBtn;
    EditText postPrice;
    EditText postDesc;
    EditText postAdd;
    EditText postZip;
    Spinner category;
    Button previewBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        cancelBtn = findViewById(R.id.buttonCancelPost);
        postTitle = findViewById(R.id.editTextPostTitle);
        postPrice = findViewById(R.id.editTextPrice);
        postDesc = findViewById(R.id.editTextPostDesc);
        category = findViewById(R.id.spinnerPostCat);
        previewBtn = findViewById(R.id.buttonPreviewPost);
        postZip = findViewById(R.id.editTextZip);
        postAdd = findViewById(R.id.editTextAddress);
        //cancel button
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreatePostActivity.this, SearchPostsActivity.class));
            }
        });

        //preview post
        previewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (postTitle.getText().toString().trim().length()==0){
                    Toast.makeText(CreatePostActivity.this, "Please give your post a title", Toast.LENGTH_SHORT).show();
                }
                else if (postDesc.getText().toString().trim().length()==0){
                    Toast.makeText(CreatePostActivity.this, "Please describe this posting", Toast.LENGTH_SHORT).show();
                }
                else if (postPrice.getText().toString().trim().length()==0){
                    Toast.makeText(CreatePostActivity.this, "Please give a price for this posting", Toast.LENGTH_SHORT).show();
                }
                else if (postAdd.getText().toString().trim().length()==0){
                    Toast.makeText(CreatePostActivity.this, "Please type in the address", Toast.LENGTH_SHORT).show();
                }
                else if (postZip.getText().toString().trim().length()==0){
                    Toast.makeText(CreatePostActivity.this, "Please type in the zip code", Toast.LENGTH_SHORT).show();
                }
                else{
                    String title = postTitle.getText().toString();
                    String desc = postDesc.getText().toString();
                    double price = 0;
                    try {
                        price = Double.parseDouble(postPrice.getText().toString());
                    }catch(Exception ex){
                        Toast.makeText(CreatePostActivity.this, "No price found", Toast.LENGTH_SHORT).show();
                    }
                    int index = category.getSelectedItemPosition();
                    String cat = category.getSelectedItem().toString();
                    String address = postAdd.getText().toString();
                    String zip = postZip.getText().toString();
                    Intent gotoPreview = new Intent(CreatePostActivity.this, PostPreview.class);
                    //create Bundle
                    Bundle myBundle =new Bundle();
                    myBundle.putString("TITLE", title);
                    myBundle.putString("DESC", desc);
                    myBundle.putString("ADD", address);
                    myBundle.putString("ZIP", zip);
                    myBundle.putInt("CAT_POS", index);
                    myBundle.putString("CAT", cat);
                    myBundle.putDouble("PRICE", price);
                    gotoPreview.putExtras(myBundle);

                    startActivityForResult(gotoPreview, LAUNCH_SECOND_ACTIVITY );
                }

            }
        });
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data){
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == LAUNCH_SECOND_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                postTitle.setText(data.getExtras().getString("rtitle"));
                postDesc.setText(data.getExtras().getString("rdes"));
                //postPrice.setText(data.getExtras().getString("rprice"));
            }
        }
    }
}