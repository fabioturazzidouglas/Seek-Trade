package com.example.seektradeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MyPostAdapter extends BaseAdapter {

    List<Post> listAllMyPosts;

    public MyPostAdapter(List<Post> allMyPosts) {
        listAllMyPosts = allMyPosts;
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
//textViewMyTitle
        TextView txtViewPostTitle = convertView.findViewById(R.id.textViewMyTitle);
        String displayText = "\n"+listAllMyPosts.get(position).getTitle() + "\n$" + listAllMyPosts.get(position).getPrice() + "\n" +listAllMyPosts.get(position).getPostDate();
        txtViewPostTitle.setText(displayText);

//        ImageView imgViewPhoto = view.findViewById(R.id.imgView_pic1);
//        imgViewPhoto.setImageResource(postList.get(i).getPhotos());
        return convertView;
    }
}
