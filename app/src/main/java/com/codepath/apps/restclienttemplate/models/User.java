package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    //create instance variables of all the data for this class
    public String name;
    public String screenName;
    public String profileImageURL;
    //empty constructor needed by the Parceler library
    public User(){}
    public static User fromJson(JSONObject jsonObject) throws JSONException {
        //Create a new user object
        User user = new User();
        //extract the values from the jsonObject
        user.name = jsonObject.getString("name");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageURL = jsonObject.getString("profile_image_url_https");
        return user;


    }
}