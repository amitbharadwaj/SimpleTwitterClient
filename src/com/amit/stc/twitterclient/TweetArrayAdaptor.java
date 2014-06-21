package com.amit.stc.twitterclient;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.stc.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class TweetArrayAdaptor extends ArrayAdapter<Tweet> {

  static class ViewHolder {
    ImageView ivProfileImage;
    TextView tvScreenName;
    TextView tvTweet;
  }

  private final ImageLoader imageLoader;
  private final int layoutResource;

  private final Context context;

  public TweetArrayAdaptor(Context context, int layoutResource, List<Tweet> tweets) {
    super(context, layoutResource, tweets);
    this.context = context;
    this.layoutResource = layoutResource;
    imageLoader = ImageLoader.getInstance();
    imageLoader.init(ImageLoaderConfiguration.createDefault(context));
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
      viewHolder.tvScreenName = (TextView) convertView.findViewById(R.id.tvSceenName);
      viewHolder.tvTweet = (TextView) convertView.findViewById(R.id.tvTweet);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    if (NetworkHelper.isNetworkAvailable(context)) {
      imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), viewHolder.ivProfileImage);
    } else {
      Toast.makeText(context, "No Internet", Toast.LENGTH_LONG).show();
    }

    viewHolder.tvScreenName.setText(tweet.getUser().getScreenName());
    viewHolder.tvTweet.setText(tweet.getBody());

    return convertView;
  }
}
