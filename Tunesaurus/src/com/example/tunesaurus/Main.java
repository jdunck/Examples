package com.example.tunesaurus;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Audio.Media;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Main extends ListActivity {
	
	SimpleCursorAdapter adapter; 

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // set up the columns we want to fetch
        String columns[] = new String[] { MediaColumns.TITLE, AudioColumns.ARTIST, MediaColumns.DATA, AudioColumns._ID };        
        // project these columns into destination TextAreas
        int tocols[] = new int[] { android.R.id.text1, android.R.id.text2 };
        // query the Media store 
        Cursor cursor = managedQuery(Media.EXTERNAL_CONTENT_URI, columns, null, null, MediaColumns.TITLE);
        // let the activity manage (and destroy) the cursor
        startManagingCursor(cursor);
        // set up the adapter on the cursor
        adapter =  new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, cursor, columns, tocols);
        // give the adapter to this list activity
        this.setListAdapter(adapter);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	Cursor cursor = (Cursor)adapter.getItem(position);
    	Toast.makeText(this, cursor.getString(2), Toast.LENGTH_LONG).show();
    	Intent intent = new Intent(this, Player.class);
    	intent.putExtra("title", cursor.getString(0));
    	intent.putExtra("artist", cursor.getString(1));
    	intent.putExtra("data", cursor.getString(2));
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    	//intent.putExtra("art", cursor.getString(3));
    	startActivity(intent);
    	
    }
}