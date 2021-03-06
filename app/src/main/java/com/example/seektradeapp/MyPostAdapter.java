package com.example.seektradeapp;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MyPostAdapter extends BaseAdapter {

    List<Post> listAllMyPosts;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Context context;

    public MyPostAdapter(List<Post> allMyPosts, Context context) {
        listAllMyPosts = allMyPosts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listAllMyPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return listAllMyPosts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.allmyposts,parent,false);
        }

        TextView txtViewPostTitle = convertView.findViewById(R.id.textViewMyTitle);
        ImageView postPhoto = convertView.findViewById(R.id.imageViewMyPost);

        if(listAllMyPosts.size()!=0) {

            String displayText = listAllMyPosts.get(position).getTitle() + "\n$" + listAllMyPosts.get(position).getPrice() + "\n" + listAllMyPosts.get(position).getPostDate();
            txtViewPostTitle.setText(displayText);

            //Set photo from firebase

            StorageReference ref = storageReference.child(listAllMyPosts.get(position).getPhoto());
            Glide.with(context).clear(postPhoto);

            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context)
                            .load(ref)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(postPhoto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        }
        else{
            Log.e("POST ADAPTER", "THE ADAPTER WORKS");
            txtViewPostTitle.setText("You have no post to display.");

        }

        return convertView;
    }
}
