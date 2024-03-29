package com.marakana.yamba;

import winterwell.jtwitter.Twitter;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener, TextWatcher
{
  private static final String TAG = "StatusActivity";
  EditText editText;
  Button updateButton;
  TextView textCount;

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.status);

    editText = (EditText) findViewById(R.id.editText);
    updateButton = (Button) findViewById(R.id.buttonUpdate);
    updateButton.setOnClickListener(this);

    textCount = (TextView) findViewById(R.id.textCount);
    textCount.setText(Integer.toString(140));
    textCount.setTextColor(Color.GREEN);
    editText.addTextChangedListener(this);
  }

  public void onClick(View v)
  {
    String status = editText.getText().toString();
    new PostToTwitter().execute(status);
    Log.d(TAG, "onClicked");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.menu, menu);
    return true;
  }

  class PostToTwitter extends AsyncTask<String, Integer, String>
  {
    @Override
    protected String doInBackground(String... statuses)
    {
      try
      {
        YambaApplication yamba = ((YambaApplication) getApplication());
        Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]);
        return status.text;
      }
      catch (Exception e)
      {
        Log.e(TAG, "Failed to connect to twitter service", e);
        return "Failed to post - check Preferences";
      }
    }

    @Override
    protected void onProgressUpdate(Integer... values)
    {
      super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result)
    {
      Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
    }
  }

  public void afterTextChanged(Editable statusText)
  {
    int count = 140 - statusText.length();
    textCount.setText(Integer.toString(count));
    textCount.setTextColor(Color.GREEN);
    if (count < 10) textCount.setTextColor(Color.YELLOW);
    if (count < 0) textCount.setTextColor(Color.RED);
  }

  public void beforeTextChanged(CharSequence s, int start, int count, int after)
  {
  }

  public void onTextChanged(CharSequence s, int start, int before, int count)
  {
  }
}