package com.amit.stc.fragments;

import android.os.Bundle;

import com.amit.stc.twitterclient.SimpleTwitterClientApp;
import com.amit.stc.twitterclient.TwitterRestClient;
import com.amit.stc.twitterclient.TwitterRestClient.TweetOrder;

public class HomeFragment extends TweetListFragment {
  private TwitterRestClient client;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    client = SimpleTwitterClientApp.getRestClient();
    populateTimeline();
  }

  @Override
  protected void populateTimeline() {
    clearAdapter();

    client.getHomeTimeline(TweetOrder.NO_ORDER, 0L, addToAdapterHandler);
  }

  @Override
  protected void loadOlderTweets(long minTweetId) {
    client.getHomeTimeline(TweetOrder.OLDER, minTweetId, addToAdapterHandler);
  }

}
