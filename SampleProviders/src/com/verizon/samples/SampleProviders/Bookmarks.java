package com.verizon.samples.SampleProviders;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.widget.SimpleCursorAdapter;

public class Bookmarks extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String columns[] = new String[] { Browser.BookmarkColumns.TITLE, Browser.BookmarkColumns.URL, Browser.BookmarkColumns._ID };
        int tocols[] = new int[] {android.R.id.text1, android.R.id.text2};
        Cursor c = managedQuery(Browser.BOOKMARKS_URI, columns, "bookmark=1", null, null);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, columns, tocols);
        setListAdapter(sca);
     }
}
