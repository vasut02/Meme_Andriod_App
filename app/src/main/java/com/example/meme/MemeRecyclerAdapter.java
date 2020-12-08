package com.example.meme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MemeRecyclerAdapter extends RecyclerView.Adapter<MemeRecyclerAdapter.MemeViewHolder> {

    Context context;
    ArrayList<Meme> memeData;

    public MemeRecyclerAdapter(Context context, ArrayList<Meme> memeData) {
        this.context = context;
        this.memeData = memeData;
    }

    @NonNull
    @Override
    public MemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MemeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemeViewHolder holder, int position) {
        //Bind Image To Image Views
        String meme_url = memeData.get(position).getmMeme_Url();

        //using Glide Library
        Glide.with(context)
                .asBitmap()
                .skipMemoryCache(true)
                .load(meme_url)
                .thumbnail(0.05f)
                .placeholder(R.drawable.insta_loader)
                .into(holder.meme_image);
    }

    @Override
    public int getItemCount() {
        if(memeData == null )return 0;
        return memeData.size();
    }

    public static class MemeViewHolder extends RecyclerView.ViewHolder{
        ImageView Like_Button;
        TextView LikeView;
        ImageView postLink_Button;
        TextView subReddit_text;
        ImageView meme_image;

        public MemeViewHolder(@NonNull View itemView) {
            super(itemView);
            Like_Button = itemView.findViewById(R.id.Like_Button);
            LikeView= itemView.findViewById(R.id.likes_no);
            postLink_Button = itemView.findViewById(R.id.postlink_button);
            subReddit_text = itemView.findViewById(R.id.subreddit);
            meme_image = itemView.findViewById(R.id.MemeImage);
        }
    }
}
