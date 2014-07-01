package com.amit.stc.adapters;

import com.amit.stc.fragments.HomeFragment;
import com.amit.stc.fragments.MentionsFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

public class MyPagerAdapter extends FragmentPagerAdapter {

  SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
  private static int NUM_ITEMS = 2;

  public MyPagerAdapter(FragmentManager fragmentManager) {
    super(fragmentManager);
  }

  // Returns total number of pages
  @Override
  public int getCount() {
    return NUM_ITEMS;
  }

  // Returns the fragment to display for that page
  @Override
  public Fragment getItem(int position) {
    switch (position) {
      case 0: // Fragment # 0 - This will show FirstFragment
        return new HomeFragment(); // HomeFragment.newInstance(0, "home"); //FirstFragment.newInstance(0, "Page # 1");
      case 1: // Fragment # 0 - This will show FirstFragment different title
        return new MentionsFragment(); // MentionsFragment.newInstance(1, "Mentions"); // FirstFragment.newInstance(1, "Page # 2");
      default:
        return null;
    }
  }

  // Returns the page title for the top indicator
  @Override
  public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0: // Fragment # 0 - This will show FirstFragment
        return "Home"; //FirstFragment.newInstance(0, "Page # 1");
      case 1: // Fragment # 0 - This will show FirstFragment different title
        return "Mentions"; // FirstFragment.newInstance(1, "Page # 2");
      default:
        return null;
    }
  }

  @Override
  public Object instantiateItem(ViewGroup container, int position) {
    Fragment fragment = (Fragment) super.instantiateItem(container, position);
    registeredFragments.put(position, fragment);
    return fragment;
  }

  @Override
  public void destroyItem(ViewGroup container, int position, Object object) {
    registeredFragments.remove(position);
    super.destroyItem(container, position, object);
  }

  public Fragment getRegisteredFragment(int position) {
    return registeredFragments.get(position);
  }

}
