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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    Button selectPhoto;
    Button uploadPhoto;
    ImageView previewPhoto;
    FirebaseStorage storage;
    StorageReference storageReference;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    static String imgPath;

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
        //Get instances for FirebaseStorage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        selectPhoto = findViewById(R.id.buttonPreviewPhoto);
        uploadPhoto = findViewById(R.id.buttonPhotoUpload);
        previewPhoto = findViewById(R.id.imageViewPreviewNewPhoto);

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

            Glide.with(ModifyMyPost.this).clear(previewPhoto);

            StorageReference ref = storageReference.child(thisPost.getPhoto());

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    Glide.with(ModifyMyPost.this)
                            .load(ref)
                            .into(previewPhoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

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
                    thisPost.setPhoto(imgPath);

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
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

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
            Toast.makeText(ModifyMyPost.this, "Photo selected, please check the preview and select upload.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(ModifyMyPost.this, "Photo successfully uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ModifyMyPost.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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