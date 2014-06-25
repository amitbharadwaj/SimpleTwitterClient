package com.amit.stc.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
  private String name;
  private long userId;
  private String screenName;
  private String profileImageUrl;

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

  public static User fromJson(JSONObject jsonObject) {
    User u = new User();
    try {
      u.name = jsonObject.getString("name");
      u.userId = jsonObject.getLong("id");
      u.screenName = "@" + jsonObject.getString("screen_name");
      u.profileImageUrl = jsonObject.getString("profile_image_url");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return u;
  }
}
