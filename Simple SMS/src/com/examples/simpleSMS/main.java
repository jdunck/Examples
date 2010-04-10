package com.examples.simpleSMS;



import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.AdapterView.AdapterContextMenuInfo;


/**
 * Demonstrates the using a list view in transcript mode
 *
 */
public class main extends ListActivity implements OnClickListener, OnKeyListener {

    private EditText mUserText;
    
    private ArrayAdapter<String> mAdapter;
    
    private ArrayList<String> mStrings = new ArrayList<String>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);
        setListAdapter(mAdapter);
        mUserText = (EditText) findViewById(R.id.userText);

        mUserText.setOnClickListener(this);
        mUserText.setOnKeyListener(this);
        
        this.registerForContextMenu(getListView());
        
        loadPhrases();
        
    }
    
    
    private void loadPhrases() {
    	String phrase;
    	Uri allPhrases = Uri.parse(
	      "content://com.examples.provider.Phrases/phrases");
    	Cursor c = managedQuery(allPhrases, null, null, null, "title desc");
    	if (c.moveToFirst()) {
    		do{
    			phrase = c.getString(c.getColumnIndex("title"));
    			mAdapter.add(phrase);
	      } while (c.moveToNext());
	   }
    }

	public void onClick(View v) {
        sendText();
    }

    private void sendText() {
        String phrase = mUserText.getText().toString();
        if (phrase != null && !phrase.equalsIgnoreCase("")) {
        	mAdapter.add(phrase);
        	mUserText.setText(null);
    		ContentValues values = new ContentValues();
    		values.put("title", phrase);
    		Uri uri = getContentResolver().insert(
    			Uri.parse(
    				"content://com.examples.provider.Phrases/phrases"), 
    	      values);
    	}
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    sendText();
                    return true;
            }
        }
        return false;
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.clear:
    	   getContentResolver().delete(
    			      Uri.parse("content://com.examples.provider.Phrases/phrases"), 
    			         null, null);
    	   mAdapter.clear();
    	   return true;
        }
        return false;
    }
    
    
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    
    public boolean onContextItemSelected(MenuItem item) {
    	  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	  String phrase = mAdapter.getItem(info.position);
    	  switch (item.getItemId()) {
    	  case R.id.remove:
    		  return true;
    	  case R.id.sendsms:
    		  Uri smsUri = Uri.parse("tel:");  
    		  Intent intent = new Intent(Intent.ACTION_VIEW, smsUri);  
    		  intent.putExtra("sms_body", phrase);  
    		  intent.setType("vnd.android-dir/mms-sms");   
    		  startActivity(intent); 
    		  return true;
    	  default:
    		  return super.onContextItemSelected(item);
    	  }
    	  
    }
}