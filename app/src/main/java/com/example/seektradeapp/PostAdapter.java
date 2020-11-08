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
       // public ImageView imgView_post;
        public TextView txtView_post_detail;
        public Button btn_viewPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

           // imgView_post = (ImageView) itemView.findViewById(R.id.imgView_post);
            txtView_post_detail = (TextView) itemView.findViewById(R.id.txtView_post_detail);
            btn_viewPost = (Button) itemView.findViewById(R.id.btn_viewpost);
        }
    }

    public PostAdapter (List<Post> posts){
        allPosts = posts;
    }



    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate custom layout
        View postView = inflater.inflate(R.layout.singlepostlayout, parent, false);

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
        TextView textView = holder.txtView_post_detail;
        textView.setText(post.getPostDate());

//        ImageView imageView = holder.imgView_post;
//        imageView.setImageResource(post.getPostid());

        Button btnView = holder.btn_viewPost;
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
