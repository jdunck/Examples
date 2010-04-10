package com.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.LiveFolders;

public class LiveActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
        final Intent intent = getIntent();
        final String action = intent.getAction();
        if(LiveFolders.ACTION_CREATE_LIVE_FOLDER.equals(action))
        {
        	final Intent resultIntent = new Intent();
        	resultIntent.setData(BlurtProvider.LIVE_URI);
        	resultIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_NAME, "Blurtin' the latest from Buzz");
        	resultIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_ICON, 
        			Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
        	resultIntent.putExtra(LiveFolders.EXTRA_LIVE_FOLDER_DISPLAY_MODE, LiveFolders.DISPLAY_MODE_LIST);
        	setResult(RESULT_OK,resultIntent);
        }
        else
        {
        	setResult(RESULT_CANCELED);
        }
        finish();
	}

}
