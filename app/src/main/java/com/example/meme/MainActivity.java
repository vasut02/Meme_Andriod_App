package com.example.meme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Meme>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MEME_LOADER_ID = 1;

    private static final String MEME_REQUEST_URL =
            "https://meme-api.herokuapp.com/gimme/15";
    /**
     * Adapter for the list of Meme
     */
    private MemeAdapter mAdapter;

    private ArrayList<Meme> MemeData;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Code For Version 2
        StringRequest memeRequest = new StringRequest(MEME_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley response " , response);

                MemeData = extractFeatureFromJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(memeRequest);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view );
        MemeRecyclerAdapter adapter = new MemeRecyclerAdapter(this , new ArrayList<Meme>());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

    /**
     * Return a list of {@link Meme} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Meme> extractFeatureFromJson(String MemeJSON) {

        Log.e("QueryUtils",""+MemeJSON);

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Meme> Memes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject jsonObj = new JSONObject(MemeJSON);

//            Extract “memes” JSONArray
            JSONArray memes = jsonObj.getJSONArray("memes");

//            Loop through each feature in the array

            for( int i = 0 ; i < memes.length() ; i++) {

//                Get earthquake JSONObject at position i
                JSONObject c = memes.getJSONObject(i);
//                Extract “Likes” for magnitude
                int Likes = c.getInt("ups");
//                Extract “Memeurl” for location
                String Meme_url = c.getString("url");
//                Extract “PostLink” for time
                String PostLink = c.getString("postLink");
//                Extract "Subreddit"
                String subreddit = c.getString("subreddit");

//                Create Earthquake java object from magnitude, location, and time
//                Add earthquake to list of earthquakes
                Memes.add( new Meme(Likes , Meme_url , PostLink , subreddit ));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return Memes;
    }

}