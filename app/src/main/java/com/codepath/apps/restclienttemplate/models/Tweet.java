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
    public String body;
    public String createdAt;
    public User user;
    public String mediaURL;
    // public List<String> mediaURL;

    //empty constructor needed by the Parceler library
    public Tweet(){}
    //Turn a json object which the tweet repesents into a java tweet object
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        //build the tweet as per the fields in the JSON object
        //Create a new tweet object
        Tweet tweet = new Tweet();
        //extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
//        if(jsonObject.has("extended_entities")){
//            JSONArray mediaArray = jsonObject.getJSONObject("extended_entities").getJSONArray("media");
//            for(int i = 0; i< mediaArray.length(); i++){
//                //for every object inside the array we grab the string with the key media_url_https
//                tweet.mediaURL.add(mediaArray.getJSONObject(i).getString("media_url_https"));
//            }
//        } else {
//            tweet.mediaURL.add("");
//        }
        if (!jsonObject.isNull("extended_entities")) {
            JSONObject entities = jsonObject.getJSONObject("extended_entities");
            JSONArray jsonArray = entities.getJSONArray("media");
            JSONObject media = jsonArray.getJSONObject(0);
            tweet.mediaURL = String.format("%s:large", media.getString("media_url_https"));
        } else {
            tweet.mediaURL = "";
        }
        Log.i("Tweet Media: ", tweet.mediaURL);
        return tweet;

    }
    //Method to return to us a list of tweet objects from a JSON array
    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        //running a for loop through the jsonarray
        //for each element in the array we're calling the from JSON method in order to add it to our list of tweet objects
        for(int i = 0; i< jsonArray.length(); i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;

    }
        public static String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return relativeDate;
        }
    }

