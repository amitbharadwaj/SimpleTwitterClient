package com.amit.stc.twitterclient;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComposeDialogue extends DialogFragment {

  private EditText etCompose;
  private TextView tvRemainingChars;
  private Button btnTweet;

  public interface ComposeDialogListener {
    void onFinishEditDialog(String inputText);
  }

  public ComposeDialogue() {
    // Empty constructor required for DialogFragment
  }

  public static ComposeDialogue newInstance(String title) {
    ComposeDialogue frag = new ComposeDialogue();
    Bundle args = new Bundle();
    args.putString("title", title);
    frag.setArguments(args);
    return frag;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_compose, container);
    etCompose = (EditText) view.findViewById(R.id.etCompose);
    btnTweet = (Button) view.findViewById(R.id.btnTweet);
    tvRemainingChars = (TextView) view.findViewById(R.id.tvRemainingChars);
    String title = getArguments().getString("title", "Your Tweet");
    getDialog().setTitle(title);
    // Show soft keyboard automatically
    etCompose.requestFocus();
    getDialog().getWindow().setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

    btnTweet.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        ComposeDialogListener listener = (ComposeDialogListener) getActivity();
        listener.onFinishEditDialog(etCompose.getText().toString());
        dismiss();
      }
    });

    etCompose.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvRemainingChars.setText(Integer.toString(140 - s.length()));
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

    return view;
  }

}
