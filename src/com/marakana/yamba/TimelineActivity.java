package com.marakana.yamba;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class TimelineActivity extends BaseActivity
{ 
  Cursor cursor;
  ListView listTimeline;
  SimpleCursorAdapter adapter;
  static final String[] FROM = { DbHelper.C_CREATED_AT, DbHelper.C_USER, DbHelper.C_TEXT };
  static final int[] TO = { R.id.textCreatedAt, R.id.textUser, R.id.textText };

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.timeline);

    if (yamba.getPrefs().getString("username", null) == null)
    { 
      startActivity(new Intent(this, PrefsActivity.class));
      Toast.makeText(this, R.string.msgSetupPrefs, Toast.LENGTH_LONG).show();
    }

    listTimeline = (ListView) findViewById(R.id.listTimeline);
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    this.setupList();
  }

  @Override
  public void onDestroy()
  {
    super.onDestroy();
    yamba.getStatusData().close();
  }

  private void setupList()
  { 
    cursor = yamba.getStatusData().getStatusUpdates();
    startManagingCursor(cursor);

    adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, FROM, TO);
    adapter.setViewBinder(VIEW_BINDER); // <6>
    listTimeline.setAdapter(adapter);
  }

  static final ViewBinder VIEW_BINDER = new ViewBinder()
  {

    public boolean setViewValue(View view, Cursor cursor, int columnIndex)
    {
      if (view.getId() != R.id.textCreatedAt) return false;

      long timestamp = cursor.getLong(columnIndex);
      CharSequence relTime = DateUtils.getRelativeTimeSpanString(view.getContext(), timestamp);
      ((TextView) view).setText(relTime);

      return true;
    }
  };
}
