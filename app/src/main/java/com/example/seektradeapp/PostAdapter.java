package com.example.seektradeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends BaseAdapter {
    List<Post> postList;


    public PostAdapter(List<Post> postList) {
        this.postList = postList;
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
            view = layoutInflater.inflate(R.layout.gridview_item,viewGroup,false);
        }

        TextView txtViewPostTitle = view.findViewById(R.id.textView_postTitle);
        txtViewPostTitle.setText(postList.get(i).getTitle());
        TextView txtViewPrice = view.findViewById(R.id.textView_price);
        txtViewPrice.setText("$" + postList.get(i).getPrice());

//        ImageView imgViewPhoto = view.findViewById(R.id.imgView_pic1);
//        imgViewPhoto.setImageResource(postList.get(i).getPhotos());
        return view;
    }
}
