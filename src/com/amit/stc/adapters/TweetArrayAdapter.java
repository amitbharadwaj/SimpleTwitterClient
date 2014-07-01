package com.amit.stc.adapters;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.stc.activities.ProfileActivity;
import com.amit.stc.models.Tweet;
import com.amit.stc.models.User;
import com.amit.stc.twitterclient.R;
import com.amit.stc.utils.NetworkHelper;
import com.amit.stc.utils.TextHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {

  private final OnClickListener imageClickListener;

  static class ViewHolder {
    ImageView ivProfileImage;
    TextView tvUserName;
    TextView tvScreenName;
    TextView tvRelativeTime;
    TextView tvTweet;
  }

  private final ImageLoader imageLoader;
  private final int layoutResource;

  private final Context context;

  public TweetArrayAdapter(final Context context, int layoutResource, List<Tweet> tweets) {
    super(context, layoutResource, tweets);
    this.context = context;
    this.layoutResource = layoutResource;
    imageLoader = ImageLoader.getInstance();
    imageLoader.init(ImageLoaderConfiguration.createDefault(context));

    imageClickListener = new OnClickListener() {

      @Override
      public void onClick(View v) {
        Intent intent = new Intent(context, ProfileActivity.class);
        User u = (User) v.getTag();
        Bundle mBundle = new Bundle();
        mBundle.putString("user_id", Long.toString(u.getUserId()));
        intent.putExtras(mBundle);
        context.startActivity(intent);
      }
    };
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    Tweet tweet = this.getItem(position);
    ViewHolder viewHolder;

    if (convertView == null) {
      LayoutInflater inflator = LayoutInflater.from(getContext());
      convertView = (View) inflator.inflate(layoutResource, parent, false);

      viewHolder = new ViewHolder();
      viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
      viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
      viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvSceenName);
      viewHolder.tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
      viewHolder.tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.ivProfileImage.setTag(tweet.getUser());
    viewHolder.ivProfileImage.setOnClickListener(imageClickListener);

    if (NetworkHelper.isNetworkAvailable(context)) {
      imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivProfileImage);
    } else {
      Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show();
    }

    viewHolder.tvUserName.setText(tweet.getUser().getName());
    viewHolder.tvScreenName.setText(tweet.getUser().getScreenName());
    viewHolder.tvRelativeTime.setText(TextHelper.getRelativeTimeAgo(tweet.getCreatedAt()));
    viewHolder.tvTweet.setText(tweet.getBody());

    return convertView;
  }
}
