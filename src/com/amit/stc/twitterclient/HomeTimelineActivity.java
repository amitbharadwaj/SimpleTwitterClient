package com.amit.stc.twitterclient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.amit.stc.models.Tweet;
import com.amit.stc.twitterclient.ComposeDialogue.ComposeDialogListener;
import com.amit.stc.twitterclient.TwitterRestClient.TweetOrder;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineActivity extends FragmentActivity implements ComposeDialogListener {
  private TwitterRestClient client;
  //  private ListView lvHomeTimeline;

  private PullToRefreshListView lvHomeTimeline;
  List<Tweet> tweets;
  TweetArrayAdaptor aTweets;

  private long maxTweetId;
  private long minTweetId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_timeline);
    lvHomeTimeline = (PullToRefreshListView) findViewById(R.id.lvHomeTimeline);
    tweets = new ArrayList<Tweet>();
    aTweets = new TweetArrayAdaptor(this, R.layout.tweet_item, tweets);
    lvHomeTimeline.setAdapter(aTweets);

    maxTweetId = Long.MIN_VALUE;
    minTweetId = Long.MAX_VALUE;

    client = SimpleTwitterClientApp.getRestClient();

    lvHomeTimeline.setOnScrollListener(new EndlessScrollListener() {
      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to your AdapterView
        loadOlderTweets();
        // or customLoadMoreDataFromApi(totalItemsCount); 
      }
    });

    lvHomeTimeline.setOnRefreshListener(new OnRefreshListener() {

      @Override
      public void onRefresh() {
        refreshTimeLine();
      }
    });

    Log.d("debug", "testing debug");
    populateTimeline();

  }

  protected void refreshTimeLine() {
    Log.d("debug", "pulled to refresh");
    populateTimeline();
    lvHomeTimeline.onRefreshComplete();
  }

  private void loadOlderTweets() {
    client.getHomeTimeline(TweetOrder.OLDER, minTweetId, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(JSONArray json) {
        addTweetsToAdapter(json);
      }

      @Override
      public void onFailure(Throwable e, String s) {
        Toast.makeText(HomeTimelineActivity.this, s, Toast.LENGTH_LONG).show();
        Log.d("error", s);
      }

    });
  }

  private void populateTimeline() {
    aTweets.clear();
    client.getHomeTimeline(TweetOrder.NO_ORDER, 0L, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(JSONArray json) {
        Toast.makeText(HomeTimelineActivity.this, "success getting json response",
            Toast.LENGTH_LONG).show();
        Log.d("debug", json.toString());
        addTweetsToAdapter(json);
      }

      @Override
      public void onFailure(Throwable e, String s) {
        Toast.makeText(HomeTimelineActivity.this, s, Toast.LENGTH_LONG).show();
        System.err.println(s);
        Log.d("debug", s);
      }
    });
  }

  protected void addTweetsToAdapter(JSONArray json) {
    Toast.makeText(HomeTimelineActivity.this, "success getting json response",
        Toast.LENGTH_LONG).show();
    Log.d("debug", json.toString());

    for (int i = 0; i < json.length(); i++) {
      Tweet tweet;
      try {
        tweet = Tweet.fromJson(json.getJSONObject(i));
        if (tweet != null) {
          aTweets.add(tweet);
          if (tweet.getTweetId() < minTweetId) {
            minTweetId = tweet.getTweetId();
          }
          if (tweet.getTweetId() > maxTweetId) {
            maxTweetId = tweet.getTweetId();
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onFinishEditDialog(String inputText) {
    // Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    client.postTweet(inputText, new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(JSONObject json) {
        addTweetToHeadOfAdapter(json);
      }

      @Override
      public void onFailure(Throwable e, String s) {
        Toast.makeText(HomeTimelineActivity.this, s, Toast.LENGTH_LONG).show();
        Log.d("error", s);
      }
    });
  }

  protected void addTweetToHeadOfAdapter(JSONObject json) {
    Tweet tweet = Tweet.fromJson(json);
    if (tweet != null) {
      aTweets.insert(tweet, 0);
    }
  }

  public void onCompose(MenuItem mi) {
    FragmentManager fm = getSupportFragmentManager();
    ComposeDialogue composeDialog = ComposeDialogue.newInstance("Compose Tweet");
    composeDialog.show(fm, "fragment_compose");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.stc, menu);
    return true;
  }

}
