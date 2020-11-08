package com.example.seektradeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private List <Post> allPosts;

    //Create view holder to give access to our view
    // Provide a direct reference to each of the views within a data item
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imgView_pic1;

        public TextView textView_postTitle;
        public TextView textView_price;
        //public Button btn_viewPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgView_pic1 = (ImageView) itemView.findViewById(R.id.imgView_pic1);
            textView_postTitle = (TextView) itemView.findViewById(R.id.textView_postTitle);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price);
            //btn_viewPost = (Button) itemView.findViewById(R.id.btn_viewpost);
        }
    }

    //constructor
    public PostAdapter (List<Post> posts){
        allPosts = posts;
    }



    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View postView = inflater.inflate(R.layout.gridview_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(postView);
        return viewHolder;
    }

//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//
//    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Post post = allPosts.get(position);

        //set item views based on the view and data model
        TextView textView_title = holder.textView_postTitle;
        textView_title.setText(post.getTitle());

        TextView textView_showprice = holder.textView_price;
        textView_showprice.setText(String.valueOf(post.getPrice()));

        ImageView imageView = holder.imgView_pic1;
        //imageView.setImageResource(post.getPostid());




    }

    @Override
    public int getItemCount() {
        return allPosts.size();
    }
//
//    public void addMorePostings(List<Post> newPosts){
//        allPosts.addAll(newPosts);
//        submitList(allPosts);
//    }




}
