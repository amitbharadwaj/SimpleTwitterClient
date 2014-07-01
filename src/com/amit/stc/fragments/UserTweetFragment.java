package com.amit.stc.fragments;

import android.os.Bundle;

import com.amit.stc.twitterclient.SimpleTwitterClientApp;
import com.amit.stc.twitterclient.TwitterRestClient;
import com.amit.stc.twitterclient.TwitterRestClient.TweetOrder;

public class UserTweetFragment extends TweetListFragment {
  private TwitterRestClient client;
  private long userId;

  public static UserTweetFragment newInstance(long l) {
    UserTweetFragment userTweetFragment = new UserTweetFragment();
    Bundle args = new Bundle();
    args.putLong("user_id", l);
    userTweetFragment.setArguments(args);
    return userTweetFragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = SimpleTwitterClientApp.getRestClient();
    userId = getArguments().getLong("user_id", 0);
    populateTimeline();
  }

  @Override
  protected void populateTimeline() {
    clearAdapter();

    client.getUserTweets(TweetOrder.NO_ORDER, userId, 0L, addToAdapterHandler);
  }

  @Override
  protected void loadOlderTweets(long minTweetId) {
    client.getUserTweets(TweetOrder.OLDER, userId, minTweetId, addToAdapterHandler);
  }

}
