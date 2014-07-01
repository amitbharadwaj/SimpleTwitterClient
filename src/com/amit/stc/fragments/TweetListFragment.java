package com.amit.stc.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amit.stc.adapters.TweetArrayAdapter;
import com.amit.stc.listeners.EndlessScrollListener;
import com.amit.stc.listeners.ProgressBarListener;
import com.amit.stc.models.Tweet;
import com.amit.stc.twitterclient.R;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TweetListFragment extends Fragment {

  private PullToRefreshListView lvTweets;
  private List<Tweet> tweets;
  private TweetArrayAdapter aTweets;

  private long maxTweetId;
  private long minTweetId;

  JsonHttpResponseHandler addToAdapterHandler;

  public static TweetListFragment newInstance(int page, String title) {
    TweetListFragment fragmentFirst = new TweetListFragment();
    Bundle args = new Bundle();
    args.putInt("page_id", page);
    args.putString("title", title);
    fragmentFirst.setArguments(args);
    return fragmentFirst;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    tweets = new ArrayList<Tweet>();
    aTweets = new TweetArrayAdapter(getActivity(), R.layout.tweet_item, tweets);

    maxTweetId = Long.MIN_VALUE;
    minTweetId = Long.MAX_VALUE;

    addToAdapterHandler = new JsonHttpResponseHandler() {

      @Override
      public void onStart() {
        ProgressBarListener pbLister = (ProgressBarListener) getActivity();
        pbLister.showProgressBar();
      }

      @Override
      public void onSuccess(JSONArray json) {
        addTweetsToAdapter(json);
      }

      @Override
      public void onFailure(Throwable e, JSONObject s) {
        Toast.makeText(getActivity(), s.toString(), Toast.LENGTH_LONG).show();
        Log.d("debug", s.toString());
      }

      @Override
      public void onFinish() {
        ProgressBarListener pbLister = (ProgressBarListener) getActivity();
        pbLister.hideProgressBar();
      }
    };

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
    lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvHomeTimeline);
    lvTweets.setAdapter(aTweets);

    lvTweets.setOnScrollListener(new EndlessScrollListener(1) {

      @Override
      public void onLoadMore(int page, int totalItemsCount) {
        // Triggered only when new data needs to be appended to the list
        // Add whatever code is needed to append new items to your AdapterView
        loadOlderTweets(minTweetId);
        // or customLoadMoreDataFromApi(totalItemsCount); 
      }
    });

    lvTweets.setOnRefreshListener(new OnRefreshListener() {

      @Override
      public void onRefresh() {
        Log.d("debug", "pulled to refresh");
        populateTimeline();
        lvTweets.onRefreshComplete();
      }
    });

    return v;
  }

  protected void populateTimeline() {
  }

  protected void loadOlderTweets(long minTweetId) {
  }

  protected void addTweetsToAdapter(JSONArray json) {
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

  public void insertTweet(Tweet tweet) {
    aTweets.insert(tweet, 0);
  }

  protected void clearAdapter() {
    tweets.clear();
    aTweets.clear();
  }

}
