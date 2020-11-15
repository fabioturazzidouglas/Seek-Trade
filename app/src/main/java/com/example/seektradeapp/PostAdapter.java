package com.example.seektradeapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    List<Post> postList;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return postList.size();
    }

    @Override
    public Object getItem(int i) {
        return postList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null){
            LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
            view = layoutInflater.inflate(R.layout.listview_item,viewGroup,false);
        }

        TextView txtViewPostTitle = view.findViewById(R.id.textView_postTitle);
        txtViewPostTitle.setText(postList.get(i).getTitle());
        TextView txtViewPrice = view.findViewById(R.id.textView_price);
        txtViewPrice.setText("$" + postList.get(i).getPrice());
        ImageView imgViewPostPhoto = view.findViewById(R.id.imgView_photo);
        StorageReference ref = storageReference.child(postList.get(i).getPhoto());

        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(ref)
                        .into(imgViewPostPhoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });




//        ImageView imgViewPhoto = view.findViewById(R.id.imgView_pic1);
//        imgViewPhoto.setImageResource(postList.get(i).getPhotos());
        return view;
    }
}
