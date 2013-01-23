package com.marakana.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApplication extends Application implements OnSharedPreferenceChangeListener
{ 
  private static final String TAG = YambaApplication.class.getSimpleName();
  public Twitter twitter;
  private SharedPreferences prefs;

  @Override
  public void onCreate()
  { 
    super.onCreate();
    this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
    this.prefs.registerOnSharedPreferenceChangeListener(this);
    Log.i(TAG, "onCreated");
  }

  @Override
  public void onTerminate()
  { 
    super.onTerminate();
    Log.i(TAG, "onTerminated");
  }

  public synchronized Twitter getTwitter()
  { 
    if (twitter == null)
    {
      String username, password, apiRoot;
      username = prefs.getString("username", "student");
      password = prefs.getString("password", "password");
      apiRoot = prefs.getString("apiRoot", "http://yamba.marakana.com/api");

      twitter = new Twitter(username, password);
      twitter.setAPIRootUrl(apiRoot);
    }
    return twitter;
  }

  public synchronized void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
  { 
    this.twitter = null;
  }
}
