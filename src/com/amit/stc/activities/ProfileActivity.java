package com.amit.stc.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amit.stc.fragments.UserTweetFragment;
import com.amit.stc.listeners.ProgressBarListener;
import com.amit.stc.models.User;
import com.amit.stc.twitterclient.R;
import com.amit.stc.twitterclient.SimpleTwitterClientApp;
import com.amit.stc.twitterclient.TwitterRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class ProfileActivity extends ActionBarActivity implements ProgressBarListener {
  private TwitterRestClient client;
  private ImageLoader imageLoader;

  ImageView ivProfile;
  TextView tvProfileName;
  TextView tvProfileDescription;
  TextView tvNumTweet;
  TextView tvFollowing;
  TextView tvFollower;

  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(R.layout.activity_profile);

    client = SimpleTwitterClientApp.getRestClient();
    imageLoader = ImageLoader.getInstance();
    imageLoader.init(ImageLoaderConfiguration.createDefault(ProfileActivity.this));

    user = null;

    ivProfile = (ImageView) findViewById(R.id.ivProfile);
    tvProfileName = (TextView) findViewById(R.id.tvProfileName);
    tvProfileDescription = (TextView) findViewById(R.id.tvProfileDescription);
    tvNumTweet = (TextView) findViewById(R.id.tvNumTweet);
    tvFollowing = (TextView) findViewById(R.id.tvFollowing);
    tvFollower = (TextView) findViewById(R.id.tvFollower);

    Bundle extras = getIntent().getExtras();
    if (extras != null && extras.containsKey("user_id")) {
      getUserProfileInfo(extras.getString("user_id"));
    } else {
      getMyProfileInfo();
    }

  }

  private void getUserProfileInfo(String userId) {
    client.getUserInfo(userId, new JsonHttpResponseHandler() {

      @Override
      public void onStart() {
        Log.d("debug", "started getProfileInfo call");
        showProgressBar();
      }

      @Override
      public void onFailure(Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(ProfileActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
        Log.d("debug", errorResponse.toString());
      }

      @Override
      public void onSuccess(JSONObject json) {
        Toast.makeText(ProfileActivity.this, "success getting json response",
            Toast.LENGTH_LONG).show();
        Log.d("debug", json.toString());
        user = User.fromJson(json);
        getSupportActionBar().setTitle(user.getScreenName());
        updateViewsFromUser();
      }

      @Override
      public void onFinish() {
        Log.d("debug", "done getProfileInfo call");
        hideProgressBar();
      }

    });

  }

  private void getMyProfileInfo() {
    client.getMyInfo(new JsonHttpResponseHandler() {

      @Override
      public void onStart() {
        Log.d("debug", "started getProfileInfo call");
        showProgressBar();
      }

      @Override
      public void onFailure(Throwable throwable, JSONObject errorResponse) {
        Toast.makeText(ProfileActivity.this, errorResponse.toString(), Toast.LENGTH_LONG).show();
        Log.d("debug", errorResponse.toString());
      }

      @Override
      public void onSuccess(JSONObject json) {
        Toast.makeText(ProfileActivity.this, "success getting json response",
            Toast.LENGTH_LONG).show();
        Log.d("debug", json.toString());
        user = User.fromJson(json);
        getSupportActionBar().setTitle(user.getScreenName());
        updateViewsFromUser();
      }

      @Override
      public void onFinish() {
        Log.d("debug", "done getProfileInfo call");
        hideProgressBar();
      }

    });
  }

  protected void updateViewsFromUser() {
    imageLoader.displayImage(user.getProfileImageUrl(), ivProfile);
    tvProfileName.setText(user.getName());
    tvNumTweet.setText(user.getNumTweets() + " Tweets");
    tvFollowing.setText(user.getNumFollowing() + " Following");
    tvFollower.setText(user.getNumFollowers() + " Followers");
    tvProfileDescription.setText(user.getDescription());

    // Begin the transaction
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    UserTweetFragment utFragment = UserTweetFragment.newInstance(user.getUserId());
    // Replace the container with the new fragment
    ft.replace(R.id.flUserTweetContainer, utFragment);
    // Execute the changes specified
    ft.commit();

  }

  // Should be called manually when an async task has started
  @Override
  public void showProgressBar() {
    setProgressBarIndeterminateVisibility(true);
  }

  // Should be called when an async task has finished
  @Override
  public void hideProgressBar() {
    setProgressBarIndeterminateVisibility(false);
  }
}
