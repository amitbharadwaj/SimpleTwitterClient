package com.amit.stc.twitterclient;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.amit.stc.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineActivity extends Activity {
  private TwitterRestClient client;
  private ListView lvHomeTimeline;
  List<Tweet> tweets;
  TweetArrayAdaptor aTweets;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_timeline);
    lvHomeTimeline = (ListView) findViewById(R.id.lvHomeTimeline);
    tweets = new ArrayList<Tweet>();
    aTweets = new TweetArrayAdaptor(this, R.layout.tweet_item, tweets);
    lvHomeTimeline.setAdapter(aTweets);

    client = SimpleTwitterClientApp.getRestClient();
    populateTimeline();
  }

  private void populateTimeline() {
    client.getHomeTimeline(new JsonHttpResponseHandler() {

      @Override
      public void onSuccess(JSONArray json) {
        List<Tweet> ts = Tweet.fromJsonArray(json);
        aTweets.addAll(ts);
        //        Toast.makeText(HomeTimelineActivity.this, "success getting json response",
        //            Toast.LENGTH_LONG).show();
        //        Log.d("debug", json.toString());
      }

      @Override
      public void onFailure(Throwable e, String s) {
        Toast.makeText(HomeTimelineActivity.this, s, Toast.LENGTH_LONG).show();
        Log.d("error", s);
      }

    });

  }
}
