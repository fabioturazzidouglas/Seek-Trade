package com.example.seektradeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    //Photo upload
    Button uploadPhoto;
    Button selectPhoto;
    ImageView previewPhoto;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    static String imgPath = "images/";

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

        //Photo upload buttons instantiation
        uploadPhoto = findViewById(R.id.buttonUploadPhoto);
        previewPhoto = findViewById(R.id.imageViewPreviewPhoto);
        selectPhoto = findViewById(R.id.buttonSelectPhoto);

        //Get instances for FirebaseStorage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();

            }
        });


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

                if (postTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Please give your post a title", Toast.LENGTH_SHORT).show();
                } else if (postDesc.getText().toString().trim().length() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Please describe this posting", Toast.LENGTH_SHORT).show();
                } else if (postPrice.getText().toString().trim().length() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Please give a price for this posting", Toast.LENGTH_SHORT).show();
                } else if (postAdd.getText().toString().trim().length() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Please type in the address", Toast.LENGTH_SHORT).show();
                } else if (postZip.getText().toString().trim().length() == 0) {
                    Toast.makeText(CreatePostActivity.this, "Please type in the zip code", Toast.LENGTH_SHORT).show();
                } else {
                    String title = postTitle.getText().toString();
                    String desc = postDesc.getText().toString();
                    double price = 0;
                    try {
                        price = Double.parseDouble(postPrice.getText().toString());
                    } catch (Exception ex) {
                        Toast.makeText(CreatePostActivity.this, "No price found", Toast.LENGTH_SHORT).show();
                    }
                    int index = category.getSelectedItemPosition();
                    String cat = category.getSelectedItem().toString();
                    String address = postAdd.getText().toString();
                    String zip = postZip.getText().toString();
                    Intent gotoPreview = new Intent(CreatePostActivity.this, PostPreview.class);
                    //create Bundle
                    Bundle myBundle = new Bundle();
                    myBundle.putString("TITLE", title);
                    myBundle.putString("DESC", desc);
                    myBundle.putString("ADD", address);
                    myBundle.putString("ZIP", zip);
                    myBundle.putInt("CAT_POS", index);
                    myBundle.putString("CAT", cat);
                    myBundle.putDouble("PRICE", price);
                    myBundle.putString("PHOTO", imgPath);
                    Log.e("CreatePostActivity", "Bundle " + imgPath);
                    gotoPreview.putExtras(myBundle);

                    startActivityForResult(gotoPreview, LAUNCH_SECOND_ACTIVITY);
                }

            }
        });
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                postTitle.setText(data.getExtras().getString("rtitle"));
                postDesc.setText(data.getExtras().getString("rdes"));
                //postPrice.setText(data.getExtras().getString("rprice"));
            }
        }

        //Show photo preview
        if (reqCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                previewPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(CreatePostActivity.this, "Photo selected, please check the preview and select upload.", Toast.LENGTH_SHORT).show();
        }
    }

    //Photo upload
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("ddmmyy_HHmmss");
            String pathComponent = sdf.format(cal.getTime()).toString();

            String imgPathFirebase = "images/" + pathComponent;
            this.imgPath = imgPathFirebase;
            StorageReference ref = storageReference.child(imgPathFirebase);

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(CreatePostActivity.this, "Photo successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(CreatePostActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}