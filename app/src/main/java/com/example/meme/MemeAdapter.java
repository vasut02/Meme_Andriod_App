package com.example.meme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.InputStream;

import java.util.ArrayList;


public class MemeAdapter extends ArrayAdapter<Meme> {


    private Context context;

    //to call super constructor
    public MemeAdapter(@NonNull Context context, @NonNull ArrayList<Meme> memes) {
        super(context, 0, memes);
        this.context = context;
    }

    //infalte view
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);

        }

        Meme current_meme = getItem(position);

        //to like
        ImageView Like_Button = listItemView.findViewById(R.id.Like_Button);
        Like_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Like_Button.setImageResource(R.drawable.outline_favorite_24);
            }
        });

        int Total_Likes = current_meme.getmLikes();
        TextView LikeView = listItemView.findViewById(R.id.likes_no);
        LikeView.setText(Total_Likes + " Likes ");


        //to open post
        ImageView postlink_Button = listItemView.findViewById(R.id.postlink_button);
        String post_url = current_meme.getmPostLink();

        postlink_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri meme_Uri = Uri.parse(post_url);

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, meme_Uri);

                // Send the intent to launch a new activity
                context.startActivity(websiteIntent);
            }
        });

        //names of subbrediit
        String subreddit = current_meme.getmSubreddit();
        TextView Subreddit_text = listItemView.findViewById(R.id.subreddit);
        TextView Subreddit_text0 = listItemView.findViewById(R.id.subreddit0);

        Subreddit_text.setText(subreddit);
        Subreddit_text0.setText(subreddit);


        //Download meme From given url
        ImageView meme_image = listItemView.findViewById(R.id.MemeImage);
        String meme_url = current_meme.getmMeme_Url();

        DownloadImageTask Image_Download = new DownloadImageTask(meme_image);
        Image_Download.execute(meme_url);


        return listItemView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;

        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            bmImage.setImageBitmap(result);


        }
    }

}
