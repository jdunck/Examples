package com.example;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity 
{
	private BuzzGrabber buzzGrabber;
	
	private static final int QUERY_TOKEN = 87;
	private static final String LIST_STATE_KEY="liststate";
	private static final String FOCUS_KEY="focused";
	public static final int BIND_DATA=42;
    /**
     * Used to keep track of the scroll state of the list.
     */
    private Parcelable listState = null;
    private boolean listHasFocus;
    private BlurtAdapter adapter;
    private QueryHandler queryHandler;
    
	private static final String[] PROJECTION = new String[]{
		BlurtColumns._ID,
		BlurtColumns.TITLE,
		BlurtColumns.DETAIL};


	private static final int INDEX_TITLE=1;
	private static final int INDEX_DETAIL=2;

	private final Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch(msg.what)
			{
			case BIND_DATA:
				startQuery();
				break;
			}
		}
	};
	
	private final ContentObserver observer = new ContentObserver(handler)
	{
		@Override
		public void onChange(boolean selfChange) 
		{
			super.onChange(selfChange);
			handler.sendEmptyMessage(BIND_DATA);
		}
	};
	
    private final class QueryHandler extends AsyncQueryHandler
    {
		public QueryHandler(Context context) 
		{
			super(context.getContentResolver());
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) 
		{
			ListView list = getListView();
			if(!isFinishing())
			{
				adapter.setLoading(false);
				adapter.changeCursor(cursor);
				
                // Now that the cursor is populated again, it's possible to restore the list state
                if (listState != null) 
                {
                    list.onRestoreInstanceState(listState);
                    if (listHasFocus) 
                    {
                        list.requestFocus();
                    }
                    listHasFocus = false;
                    listState = null;
                }
			}
			else
			{
				cursor.close();
			}
		}
    }
	
	private void hookObservers()
	{
		this.getContentResolver().registerContentObserver(BlurtProvider.CONTENT_URI, true, observer);
	}
	
	private void unhookObservers()
	{
		this.getContentResolver().unregisterContentObserver(observer);
	}
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
                
        //instantiate the grabber
        buzzGrabber = new BuzzGrabber(this,"wilhewoot");
        setContentView(R.layout.main);
		//setup UI
		final ListView list = getListView();
		list.setFocusable(true);
		list.setSaveEnabled(false);
		queryHandler = new QueryHandler(this);
		adapter = new BlurtAdapter(this,R.layout.blurt_item,null);
		setListAdapter(adapter);
		hookObservers();
    }
    
	@Override
	protected void onResume() 
	{
		super.onResume();
		buzzGrabber.start();
		startQuery();
	}
 
    @Override
    protected void onStop() 
    {
        super.onStop();
        
        // We don't want the list to display the empty state, since when we resume it will still
        // be there and show up while the new query is happening. After the async query finished
        // in response to onResume() setLoading(false) will be called.
        adapter.setLoading(true);
        adapter.changeCursor(null);
        buzzGrabber.stop();
    }
    
    @Override
	protected void onDestroy() 
    {
		super.onDestroy();
        unhookObservers();
        buzzGrabber.dispose();
	}
    
	@Override
    protected void onSaveInstanceState(Bundle icicle) 
    {
        super.onSaveInstanceState(icicle);
        ListView list = this.getListView();
        // Save list state in the bundle so we can restore it after the QueryHandler has run
        icicle.putParcelable(LIST_STATE_KEY, list.onSaveInstanceState());
        icicle.putBoolean(FOCUS_KEY, list.hasFocus());
    }

    @Override
    protected void onRestoreInstanceState(Bundle icicle) 
    {
        super.onRestoreInstanceState(icicle);
        // Retrieve list state. This will be applied after the QueryHandler has run
        listState = icicle.getParcelable(LIST_STATE_KEY);
        listHasFocus = icicle.getBoolean(FOCUS_KEY);
    }
    
	private void startQuery()
	{
		adapter.setLoading(true);
		//cancel any pending queries
		queryHandler.cancelOperation(QUERY_TOKEN);

		String sortOrder = BlurtColumns.TITLE + " ASC";
		queryHandler.startQuery(QUERY_TOKEN, null, BlurtProvider.CONTENT_URI, PROJECTION, null, null, sortOrder);
	}
	
    final class BlurtItem
    {
    	public TextView titleView;
    	public TextView detailView;
    	public int id=0;
    	public CharArrayBuffer title=new CharArrayBuffer(8);
    	public CharArrayBuffer detail = new CharArrayBuffer(12);
    }
    
    final class BlurtAdapter extends ResourceCursorAdapter
    {
    	private boolean isLoading=false;
    	public BlurtAdapter(Context ctx, int layout, Cursor cursor)
    	{
    		super(ctx,layout,cursor);
    	}
    	
		public void setLoading(boolean value)
		{
			isLoading=value;
		}
		
		@Override
		public boolean isEmpty() 
		{
			if(isLoading)
			{
				return false;
			}
			else
			{
				return super.isEmpty();
			}
		}
		
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) 
		{
			final View view =  super.newView(context, cursor, parent);
			final BlurtItem cache = new BlurtItem();
			cache.titleView = (TextView)view.findViewById(R.id.title);
			cache.detailView = (TextView)view.findViewById(R.id.detail);
			view.setTag(cache);
			return view;
		}
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) 
		{
			final BlurtItem cache = (BlurtItem)view.getTag();
			cursor.copyStringToBuffer(INDEX_TITLE, cache.title);
			int size = cache.title.sizeCopied;
			//Spanned text;
			if(size >-1)
			{
				cache.titleView.setText(cache.title.data,0,size);
			}
			else
			{
				cache.titleView.setText("Oh no you didn't!");
			}
			cursor.copyStringToBuffer(INDEX_DETAIL, cache.detail);
			size = cache.detail.sizeCopied;
			if(size >-1)
			{
				//text = Html.fromHtml(new String(cache.detail.data));
				cache.detailView.setText(cache.detail.data,0,size);
			}
			else
			{
				cache.detailView.setText("Nothing to see here.");
			}
		}
    }
}