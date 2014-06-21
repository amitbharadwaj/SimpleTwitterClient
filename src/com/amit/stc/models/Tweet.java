package com.amit.stc.models;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.util.Log;

public class Tweet {
  private String body;
  private long tweetId;
  private String createdAt;
  private User user;

  public String getBody() {
    return body;
  }

  public long getTweetId() {
    return tweetId;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public User getUser() {
    return user;
  }

  public static Tweet fromJson(JSONObject jsonObject) {
    Tweet tweet = new Tweet();
    try {
      tweet.body = jsonObject.getString("text");
      tweet.tweetId = jsonObject.getLong("id");
      tweet.createdAt = jsonObject.getString("created_at");
      tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
    } catch (JSONException e) {
      Log.e("error", e);
      return null;
    }
    return tweet;
  }

  public static List<Tweet> fromJsonArray(JSONArray jsonArray) {
    List<Tweet> tweets = new ArrayList<Tweet>();
    for (int i = 0; i < jsonArray.length(); i++) {
      try {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        tweets.add(Tweet.fromJson(jsonObject));
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    return tweets;
  }

}
