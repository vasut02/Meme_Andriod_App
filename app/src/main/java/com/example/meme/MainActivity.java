package com.example.meme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Meme>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MEME_LOADER_ID = 1;

    private static final String MEME_REQUEST_URL =
            "https://meme-api.herokuapp.com/gimme/50";
    /**
     * Adapter for the list of Meme
     */
    private MemeAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        ListView MemeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new MemeAdapter(this, new ArrayList<Meme>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        MemeListView.setAdapter(mAdapter);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(MEME_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<List<Meme>> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a new loader for the given URL
        return new MemeLoader(this, MEME_REQUEST_URL);

    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Meme>> loader, List<Meme> Memes) {
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (Memes != null && !Memes.isEmpty()) {
            mAdapter.addAll(Memes);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Meme>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


}