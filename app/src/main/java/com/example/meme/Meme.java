package com.example.meme;

public class Meme {

    int mLikes;
    String mMeme_Url;
    String mPostLink;
    String mSubreddit;

    public Meme(int mLikes, String mMeme_Url, String mPostLink, String mSubreddit) {
        this.mLikes = mLikes;
        this.mMeme_Url = mMeme_Url;
        this.mPostLink = mPostLink;
        this.mSubreddit = mSubreddit;
    }

    public int getmLikes() {
        return mLikes;
    }

    public String getmMeme_Url() {
        return mMeme_Url;
    }

    public String getmPostLink() {
        return mPostLink;
    }

    public String getmSubreddit() {
        return mSubreddit;
    }
}
