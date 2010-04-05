package com.verizon.samples.SampleProviders;

import java.util.Date;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class History extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String columns[] = new String[] { Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.DATE, Browser.BookmarkColumns._ID };
        Cursor c = managedQuery(Browser.BOOKMARKS_URI, columns, "bookmark!=1", null, null);
        CursorAdapter ca = new HistoryAdapter(this, c);
        setListAdapter(ca);
    }
      
    public class HistoryAdapter extends CursorAdapter {

		public HistoryAdapter(Context context, Cursor c) {
			super(context, c);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			TextView tv;
			tv = (TextView)view.findViewById(android.R.id.text1);
			tv.setText(cursor.getString(0)); //index of title in the projection
			
			tv = (TextView)view.findViewById(android.R.id.text2);
			long l = cursor.getLong(1); // index of date in the projection
			Date d = new Date(l);
			tv.setText(d.toString());		
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			View view = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, null);
			bindView(view, context, cursor);
			return view;
		}
    }
}
