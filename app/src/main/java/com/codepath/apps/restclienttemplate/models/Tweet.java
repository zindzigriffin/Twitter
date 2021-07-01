package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {
    //Create instance variables of each item being used in the Tweet class
    public String body;
    public String createdAt;
    public User user;
    public String mediaURL;

    //empty constructor needed by the Parceler library
    public Tweet(){}
    //Turn a json object which the tweet represents into a java tweet object
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        //build the tweet as per the fields in the JSON object
        //Create a new tweet object
        Tweet tweet = new Tweet();
        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        //if statement to check to see if the jsonObject is not null at the key "extended_entities"
        if (!jsonObject.isNull("extended_entities")) {
            //Given the key "extended entities", this line retrieves the associated object
            JSONObject entities = jsonObject.getJSONObject("extended_entities");
            //Gets the jsonArray with the associated key value "media" and returns the value mapped by the json
            JSONArray jsonArray = entities.getJSONArray("media");
            //Returns the value mapped by the json if it exists and returns a jsonObject
            JSONObject media = jsonArray.getJSONObject(0);
            //Returns a formatted string of the mediaURL and returns the value mapped by the name "media_url_https" if it exists.
            tweet.mediaURL = String.format("%s:large", media.getString("media_url_https"));
        } else {
            //if the jsonobject is null then set the mediaURL to an empty string
            tweet.mediaURL = "";
        }
        //Log statement to see if the jsonObject is getting the mediaURL appropriately for the purposes of testing
        Log.i("Tweet Media: ", tweet.mediaURL);
        //returning the tweet object
        return tweet;

    }
    //Method to return to us a list of tweet objects from a JSON array
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        //Create an arrayList of Tweets
        List<Tweet> tweets = new ArrayList<>();
        //for each element in the array we're calling the from JSON method in order to add it to our list of tweet objects
        for(int i = 0; i< jsonArray.length(); i++){
            //convert each object to a tweet model and add that tweet model to our list of tweet objects
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        //return the list of tweets
        return tweets;

    }
    //Method to get the timestamp of each tweet
        public static String getRelativeTimeAgo(String rawJsonDate) {
            // Date format for twitter
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            //Constructs a date format using the provided pattern and the default date format symbols for the given locale
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            //Specifies whether or not the date/time parsing is lenient meaning that parsing can use heuristics to determine inputs that do not match the specified format.
            sf.setLenient(true);
            //Declaration of relativeDate to an empty string
            String relativeDate = "";
            try {

                long dateMillis = sf.parse(rawJsonDate).getTime();
                //DateUtils is a class that contains info for date/time
                //getRelativeTimeSpanString returns a string that describes 'time' as relative to 'now'
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        //Returns the current time in milliseconds and converts it to a string
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            } catch (ParseException e) {
                //print the error if one has occurred while parsing
                e.printStackTrace();
            }
            //return the date
            return relativeDate;
        }
    }

