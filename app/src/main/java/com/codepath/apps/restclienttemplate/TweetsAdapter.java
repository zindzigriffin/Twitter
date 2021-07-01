package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{
    private static final String TAG = "TweetsAdapter";
    Context context;
    List<Tweet> tweets;
    //Pass in the context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets){
        this.context = context;
        this.tweets = tweets;
    }
    //For each row, inflate the layout
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //Instantiates a layout XML file and inflate a new view hierarchy from the specified xml resource.
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet,parent, false);
        //wrap the view inside of the other view holder
        return new ViewHolder(view);
    }


    //Bind values based on the position of the element
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //Get the data at the position
        Tweet tweet = tweets.get(position);
        //Bind the tweet with view holder
        holder.bind(tweet);


    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Create the instance variables
        ImageView ivProfileImage;
        TextView tvBody;
        TextView tvScreenName;
        TextView tvTimestamp;
        ImageView ivImage;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            //get references to each instance variable using findviewbyID
            //which finds the first descendant view with the given ID, the view itself if the ID matches or null if the ID is invalid
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
        //get this piece of information and bind it to
        public void bind(Tweet tweet) {
            //Sets the text to be displayed
            tvBody.setText(tweet.body);
            //Sets the text to be displayed
            tvScreenName.setText(tweet.user.screenName);
            //Using glide to load the users image profile
            Glide.with(context).load(tweet.user.profileImageURL).into(ivProfileImage);
            //Sets the text for the time stamp that which they got the relative time ago method
            tvTimestamp.setText(Tweet.getRelativeTimeAgo(tweet.createdAt));
            Log.d(TAG, "bind: " + tweet.mediaURL);
            //mediaurl is not
            if (tweet.mediaURL != ""){
                //set the image view setVisbility
                ivImage.setVisibility(View.VISIBLE);
                //Using glide to load the mediaURL
                Glide.with(context).load(tweet.mediaURL).into(ivImage);
            }
            else{
                //if not the setVisibility
                ivImage.setVisibility(View.GONE);
            }

        }
        //Clean all elements of the recycler
        public void clear() {
            tweets.clear();
            notifyDataSetChanged();
        }
        // Add a list of items -- change to type used
        public void addAll(List<Tweet> list) {
            tweets.addAll(list);
            notifyDataSetChanged();
        }
    }
}
