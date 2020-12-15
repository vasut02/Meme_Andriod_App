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
    private OnMemeListener mOnMemeListener;

    public MemeRecyclerAdapter(Context context, ArrayList<Meme> memeData , OnMemeListener onMemeListener) {
        this.context = context;
        this.memeData = memeData;
        this.mOnMemeListener = onMemeListener;
    }

    @NonNull
    @Override
    public MemeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        return new MemeViewHolder(view , mOnMemeListener);
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

        //Set Likes Count
        int Total_Likes = memeData.get(position).getmLikes();
        holder.LikeView.setText(Total_Likes + " Likes");

        //Set names of subbrediit
        String subreddit = memeData.get(position).getmSubreddit();
        holder.Subreddit_text.setText(subreddit);
        holder.Subreddit_text0.setText(subreddit);
    }

    @Override
    public int getItemCount() {
        if(memeData == null )return 0;
        return memeData.size();
    }

    public static class MemeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView Like_Button;
        TextView LikeView;
        ImageView postLink_Button;
        ImageView meme_image;
        TextView Subreddit_text;
        TextView Subreddit_text0;
        OnMemeListener onMemeListener;

        public MemeViewHolder(@NonNull View itemView , OnMemeListener onMemeListener ) {
            super(itemView);
            Like_Button = itemView.findViewById(R.id.Like_Button);
            LikeView= itemView.findViewById(R.id.likes_no);
            postLink_Button = itemView.findViewById(R.id.postlink_button);
            meme_image = itemView.findViewById(R.id.MemeImage);
            Subreddit_text = itemView.findViewById(R.id.subreddit);
            Subreddit_text0 = itemView.findViewById(R.id.subreddit0);
            this.onMemeListener = onMemeListener;

            //Set Onclicklistiner for Browser Intent
            postLink_Button.setOnClickListener(this);

            //Set Onclicklistiner for Like_Button
            Like_Button.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if ( v.getId() == R.id.postlink_button ) {
                onMemeListener.onPostLinkButtonClick(getAdapterPosition());
            }else if( v.getId() == R.id.Like_Button ) {
                onMemeListener.onLikeButtonCLick(getAdapterPosition(), v);
            }
        }
    }

    public interface OnMemeListener {
        void onPostLinkButtonClick( int position );
        void onLikeButtonCLick( int position  , View view);
    }
}
