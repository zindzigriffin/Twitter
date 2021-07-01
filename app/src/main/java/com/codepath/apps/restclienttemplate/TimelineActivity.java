package com.codepath.apps.restclienttemplate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {
    private SwipeRefreshLayout swipeContainer;
    //set the TAG to the current file name
    public static final String TAG = "TimelineActivity";
    //set the request code to 20
    private final int REQUEST_CODE = 20;
    //Creates instance variables of the items being used in this file
    TwitterClient  client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Set the activity content from a layout resourc.
        setContentView(R.layout.activity_timeline);
        Log.d("TimelineActivity", "In timeline activity");
        // Making the API request to get the home timeline
        client = TwitterApp.getRestClient(this);
        //set swipecontainer to swipe referesh layout which is used whenever the user can refresh the contents of a view using the vertical swipe gesture
        //findviewbyid finds a view that was identified by the xml attribute
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        //set the listener to be notified when a refresh is triggered via the swipe gesture.
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Calls the populateHomeTimeLine method
                populateHomeTimeline();


            }
        });
        //Set the color resources used in the progress animation from color resources.
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        //Find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //Find the logoutButton
        logoutButton = findViewById(R.id.logoutButton);
        //Initialize the list of tweets and adapter
        tweets = new ArrayList<>();
        //Set the adapter
        adapter = new TweetsAdapter(this, tweets);
        //Recycler view setup: layout manager and the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        //Set a new adapter to provide views on demand. When the adapter is changed all existing views are put back into the pool
        rvTweets.setAdapter(adapter);
        //Calls populateHomeTimeLine
        populateHomeTimeline();
        //when the logout button is clicked logout
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutButton();
            }
        });


    }

    private void onLogoutButton() {
        client.clearAccessToken(); // forget who's logged in
        finish(); // navigate backwards to Login screen

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        //if the menu item that was selected is the same as the id compose
        if(item.getItemId() == R.id.compose ) {
            //Navigate to the compose activity
            Intent intent = new Intent(this, ComposeActivity.class);
            //startActivityForResult modifies the behavior of the intent to allow results to be delivered to fragments.
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        //Called whenever an item in the options menu is selected
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        //use an if statement to check and see if the requestcodes are the same
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            //Get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            //Update the recycler view with the tweet
            //Modify data source of tweets by inserting the specified element, tweet at the specified index 0.
            tweets.add(0, tweet);
            //Notify the adapter that an item has been inserted at index 0
            adapter.notifyItemInserted(0);
            //Starts a smooth scroll to the first position of the adapter
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateHomeTimeline() {
        //Make an API request to get the hometimeline
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSucesss!" +json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    //Removes all of the elements from this last
                    tweets.clear();
                    //Appends all of the items from the Tweet.jsonArray to the list of tweets
                    tweets.addAll(Tweet.fromJsonArray(jsonArray));
                    //Notify the adapter that the data set has changed
                    adapter.notifyDataSetChanged();
                    //Notify the swipeContainer widget that the refresh state has changed.
                    swipeContainer.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "json exception", e);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure!" + response, throwable);

            }
        });
    }
}