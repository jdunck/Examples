package com.verizon.samples.SampleProviders;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class Main extends ListActivity {
	BaseAdapter adapter;
	ListView list; 
	FrameLayout frame; 
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        String[] demos = new String[] {"Contacts", "Call Log", "Browser Bookmarks", "Browser History", "Videos", "Photos", "Sms Inbox","Calendar"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, demos);
        setListAdapter(adapter);  
     }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch(position)
		{
		case 0 :
			launchActivity(Contacts.class);
			break; 
		case 1 :
			launchActivity(CallLog.class);
			break; 
		case 2 :
			launchActivity(Bookmarks.class);
			break; 
		case 3 :
			launchActivity(History.class);
			break; 
		case 4 :
			launchActivity(Videos.class);
			break; 
		case 5 :
			launchActivity(Photos.class);
			break; 
		case 6 :
			launchActivity(SmsInbox.class);
			break; 
		case 7 :
			launchActivity(Calendar.class);
			break; 
		}
	}

	private void launchActivity(Class<?> activityclass) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setClass(this, activityclass);
		startActivity(i);
	}
}