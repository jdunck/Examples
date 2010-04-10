package com.example.quicktext;


import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Main extends ListActivity {
	
	String[] items = new String[] {
			"Hey Dude, What's up",
			"Let's go for beers.",
			"I'm at the GTUG" 
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
   
    	Intent i = new Intent(Intent.ACTION_SEND);
    	i.setType("text/plain");
    	i.putExtra(Intent.EXTRA_TEXT, items[position]);
    	startActivity(i);
    }
}