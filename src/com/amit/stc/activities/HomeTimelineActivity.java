package com.amit.stc.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.amit.stc.adapters.MyPagerAdapter;
import com.amit.stc.fragments.ComposeDialogue;
import com.amit.stc.fragments.HomeFragment;
import com.amit.stc.listeners.ComposeDialogListener;
import com.amit.stc.listeners.ProgressBarListener;
import com.amit.stc.models.Tweet;
import com.amit.stc.twitterclient.R;
import com.amit.stc.twitterclient.SimpleTwitterClientApp;
import com.amit.stc.twitterclient.TwitterRestClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.viewpagerindicator.TitlePageIndicator;

public class HomeTimelineActivity extends ActionBarActivity implements ComposeDialogListener, ProgressBarListener {
  //  private static final String HOME_TAG = "Home";
  private TwitterRestClient client;

  FragmentPagerAdapter adapterViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
    setContentView(R.layout.activity_home_timeline);
    client = SimpleTwitterClientApp.getRestClient();

    // setupTabs();

    ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
    adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
    vpPager.setAdapter(adapterViewPager);

    TitlePageIndicator indicator = (TitlePageIndicator) findViewById(R.id.tpIndicator);
    indicator.setViewPager(vpPager);

  }

  //  @SuppressWarnings("unused")
  //  private void setupTabs() {
  //    ActionBar actionBar = getSupportActionBar();
  //    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
  //    actionBar.setDisplayShowTitleEnabled(true);
  //
  //    Tab tab1 = actionBar
  //        .newTab()
  //        .setText("Home")
  //        .setIcon(R.drawable.ic_home)
  //        .setTag("HomeTimelineFragment")
  //        .setTabListener(new SupportFragmentTabListener<HomeFragment>(R.id.flContainer, this,
  //            HOME_TAG, HomeFragment.class));
  //
  //    actionBar.addTab(tab1);
  //    actionBar.selectTab(tab1);
  //
  //    Tab tab2 = actionBar
  //        .newTab()
  //        .setText("Mentions")
  //        .setIcon(R.drawable.ic_mention)
  //        .setTag("MentionsTimelineFragment")
  //        .setTabListener(new SupportFragmentTabListener<MentionsFragment>(R.id.flContainer, this,
  //            "Mentions", MentionsFragment.class));
  //    actionBar.addTab(tab2);
  //  }

  @Override
  public void onFinishEditDialog(String inputText) {
    // Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    client.postTweet(inputText, new JsonHttpResponseHandler() {

      @Override
      public void onStart() {
        showProgressBar();
      }

      @Override
      public void onSuccess(JSONObject json) {
        addTweetToHeadOfAdapter(json);
      }

      @Override
      public void onFailure(Throwable e, JSONObject s) {
        Toast.makeText(HomeTimelineActivity.this, s.toString(), Toast.LENGTH_LONG).show();
        Log.d("error", s.toString());
      }

      @Override
      public void onFinish() {
        hideProgressBar();
      }
    });
  }

  protected void addTweetToHeadOfAdapter(JSONObject json) {
    Tweet tweet = Tweet.fromJson(json);
    if (tweet != null) {
      // TODO this is a hack
      HomeFragment homeFragement =
          (HomeFragment) ((MyPagerAdapter) adapterViewPager).getRegisteredFragment(0);
      // (HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_TAG);
      homeFragement.insertTweet(tweet);
    }
  }

  public void composeTweet() {
    FragmentManager fm = getSupportFragmentManager();
    ComposeDialogue composeDialog = ComposeDialogue.newInstance("Compose Tweet");
    composeDialog.show(fm, "fragment_compose");
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem mi) {
    // Handle presses on the action bar items
    switch (mi.getItemId()) {
      case R.id.action_compose:
        composeTweet();
        return true;
      case R.id.action_profile:
        showProfileActivity();
      default:
        return super.onOptionsItemSelected(mi);
    }
  }

  private void showProfileActivity() {
    Intent intent = new Intent(this, ProfileActivity.class);
    startActivity(intent);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.stc, menu);
    return super.onCreateOptionsMenu(menu);
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
