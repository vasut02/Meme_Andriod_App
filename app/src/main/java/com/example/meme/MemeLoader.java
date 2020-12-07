package com.example.meme;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */

public class MemeLoader extends AsyncTaskLoader<List<Meme>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = MemeLoader.class.getName();

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new {@link MemeLoader}.
     *
     * @param context of the activity
     * @param url     to load data from
     */
    public MemeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Meme> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<Meme> Memes = QueryUtils.fetchMemeData(mUrl);
        return Memes;
    }
}