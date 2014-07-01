package com.amit.stc.listeners;

public interface ProgressBarListener {
  // Should be called manually when an async task has started
  public void showProgressBar();

  // Should be called when an async task has finished
  public void hideProgressBar();
}
