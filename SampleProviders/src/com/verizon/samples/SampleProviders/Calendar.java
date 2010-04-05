package com.verizon.samples.SampleProviders;

import android.app.ListActivity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class Calendar extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String columns[] = new String[] { "title", "description", "_id" };
        int tocols[] = new int[] {android.R.id.text1, android.R.id.text2};
        Cursor c = managedQuery(Uri.parse("content://calendar/events"), columns, null, null, null);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, columns, tocols);
        setListAdapter(sca);
     }
}
