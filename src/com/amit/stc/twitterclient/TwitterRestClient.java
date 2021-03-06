package com.amit.stc.twitterclient;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API.
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes:
 * https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 */
public class TwitterRestClient extends OAuthBaseClient {
  public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
  public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
  public static final String REST_CONSUMER_KEY = "uJyjfeEsGZ756CiIToS9kA9O2";       // Change this
  public static final String REST_CONSUMER_SECRET =
      "hLGVUr6rOekf743NxCLHuIrsqSmy8H4cUpMZvLDleGz6DSceDp"; // Change this
  public static final String REST_CALLBACK_URL = "stc://timeline"; // Change this (here and in manifest)

  //  private Header[] headers;

  public TwitterRestClient(Context context) {
    super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET,
        REST_CALLBACK_URL);
    //    Header header1 = new BasicHeader("Content-Type", "application/x-www-form-urlencoded");
    //    Header header2 = new BasicHeader("ssl_verifypeer", "TRUE");
    //
    //    headers = new Header[] { header1, header2 };

  }

  public enum TweetOrder {
    NEWER,
    OLDER,
    NO_ORDER,
  }

  public void getHomeTimeline(TweetOrder tweetOrder, long tweetId,
      AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/home_timeline.json");
    RequestParams params = new RequestParams();
    switch (tweetOrder) {
      case NEWER:
        params.put("since_id", Long.toString(tweetId));
        break;
      case OLDER:
        params.put("max_id", Long.toString(tweetId - 1));
        break;
      default:
        params.put("since_id", "1");
        break;
    }
    client.get(apiUrl, params, handler);
    //    client.get(context, apiUrl, headers, params, handler);
  }

  public void getMentionTimeline(TweetOrder tweetOrder, long tweetId,
      AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    RequestParams params = new RequestParams();
    switch (tweetOrder) {
      case NEWER:
        params.put("since_id", Long.toString(tweetId));
        break;
      case OLDER:
        params.put("max_id", Long.toString(tweetId - 1));
        break;
      default:
        params.put("since_id", "1");
        break;
    }
    client.get(apiUrl, params, handler);
  }

  public void getUserTweets(TweetOrder tweetOrder, long userId, long tweetId,
      JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/user_timeline.json");
    RequestParams params = new RequestParams();
    params.put("user_id", Long.toString(userId));
    switch (tweetOrder) {
      case NEWER:
        params.put("since_id", Long.toString(tweetId));
        break;
      case OLDER:
        params.put("max_id", Long.toString(tweetId - 1));
        break;
      default:
        params.put("since_id", "1");
        break;
    }
    client.get(apiUrl, params, handler);
  }

  public void postTweet(String tweetText, AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("statuses/update.json");
    RequestParams params = new RequestParams();
    params.put("status", tweetText);
    client.post(apiUrl, params, handler);
  }

  public void getMyInfo(AsyncHttpResponseHandler handler) {
    String apiUrl = getApiUrl("account/verify_credentials.json");
    client.get(apiUrl, null, handler);
  }

  public void getUserInfo(String userId, JsonHttpResponseHandler handler) {
    String apiUrl = getApiUrl("users/show.json");
    RequestParams params = new RequestParams();
    params.put("user_id", userId);
    client.get(apiUrl, params, handler);
  }

  //
  //  // CHANGE THIS
  //  // DEFINE METHODS for different API endpoints here
  //  public void getInterestingnessList(AsyncHttpResponseHandler handler) {
  //    String apiUrl = getApiUrl("?nojsoncallback=1&method=flickr.interestingness.getList");
  //    // Can specify query string params directly or through RequestParams.
  //    RequestParams params = new RequestParams();
  //    params.put("format", "json");
  //    client.get(apiUrl, params, handler);
  //  }

  /* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
   * 	  i.e getApiUrl("statuses/home_timeline.json");
   * 2. Define the parameters to pass to the request (query or body)
   *    i.e RequestParams params = new RequestParams("foo", "bar");
   * 3. Define the request method and make a call to the client
   *    i.e client.get(apiUrl, params, handler);
   *    i.e client.post(apiUrl, params, handler);
   */
}
