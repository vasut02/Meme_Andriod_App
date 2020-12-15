package com.example.meme;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
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

public class MainActivity extends AppCompatActivity implements MemeRecyclerAdapter.OnMemeListener {


    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MEME_LOADER_ID = 1;

    private static final String MEME_AUTHORITY = "meme-api.herokuapp.com";
    private static final String TAG = "MainActivity" ;
    private static String MEME_REQUEST_URL =
            "https://meme-api.herokuapp.com/gimme/15";

    private static final String MEME_DEFAULT_REQUEST_URL =
            "https://meme-api.herokuapp.com/gimme/15";


    private MemeRecyclerAdapter adapter;
    private ArrayList<Meme> MemeData;

    private String JSONString ;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent() != null ){
            MEME_REQUEST_URL = handleIntent(getIntent());
        }else {
            MEME_REQUEST_URL = MEME_DEFAULT_REQUEST_URL;
        }

        //Code For Version 2
        StringRequest memeRequest = new StringRequest(MEME_REQUEST_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Volley response " , response);
                JSONString = response;

                // get data  form JSON in ArrayList
                MemeData = extractFeatureFromJson(JSONString);

                //find recycler view
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view );

                //initiate adapter
                adapter = new MemeRecyclerAdapter(MainActivity.this , MemeData , MainActivity.this) ;

                recyclerView.setHasFixedSize(true);

                // Layout Manager
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                // Attach Adapter
                recyclerView.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
            }
        });


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(memeRequest);


    }



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

    @Override
    public void onPostLinkButtonClick(int position) {
        String post_url = MemeData.get(position).getmPostLink();
        // Convert the String URL into a URI object (to pass into the Intent constructor)
        Uri meme_Uri = Uri.parse(post_url);

        // Create a new intent to view the earthquake URI
        Intent websiteIntent = new Intent(Intent.ACTION_VIEW, meme_Uri);

        // Send the intent to launch a new activity
        startActivity(websiteIntent);
    }

    @Override
    public void onLikeButtonCLick(int position , View view) {
        ImageView Like_Button = (ImageView) view;
        Like_Button.setImageResource(R.drawable.outline_favorite_24);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        }
        SearchView searchView =
                (SearchView) menu.findItem(R.id.meme_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private String handleIntent(Intent intent) {
        String myUrl = MEME_DEFAULT_REQUEST_URL;

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("https")
                    .authority(MEME_AUTHORITY)
                    .appendPath("gimme")
                    .appendPath(query)
                    .appendPath("4");
             myUrl = builder.build().toString();
        }
        Log.d(TAG, "handleIntent: " + myUrl);
        return myUrl;
    }


}