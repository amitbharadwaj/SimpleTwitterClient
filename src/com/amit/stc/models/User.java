package com.amit.stc.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
  private String name;
  private long userId;
  private String screenName;
  private String profileImageUrl;
  private String description;
  private int numTweets;
  private int numFollowing;
  private int numFollowers;

  public String getName() {
    return name;
  }

  public long getUserId() {
    return userId;
  }

  public String getScreenName() {
    return screenName;
  }

  public String getProfileImageUrl() {
    return profileImageUrl;
  }

  public String getDescription() {
    return description;
  }

  public int getNumTweets() {
    return numTweets;
  }

  public int getNumFollowing() {
    return numFollowing;
  }

  public int getNumFollowers() {
    return numFollowers;
  }

  public static User fromJson(JSONObject jsonObject) {
    User u = new User();
    try {
      u.name = jsonObject.getString("name");
      u.userId = jsonObject.getLong("id");
      u.screenName = "@" + jsonObject.getString("screen_name");
      u.profileImageUrl = jsonObject.getString("profile_image_url");
      u.description = jsonObject.getString("description");
      u.numTweets = jsonObject.has("statuses_count") ? jsonObject.getInt("statuses_count") : 0;
      u.numFollowing = jsonObject.has("friends_count") ? jsonObject.getInt("friends_count") : 0;
      u.numFollowers = jsonObject.has("followers_count") ? jsonObject.getInt("followers_count") : 0;
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return u;
  }
}
