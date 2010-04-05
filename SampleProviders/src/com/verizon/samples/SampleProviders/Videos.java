package com.verizon.samples.SampleProviders;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;
import android.provider.MediaStore.Video.*;

public class Videos  extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String columns[] = new String[] { VideoColumns.DISPLAY_NAME, VideoColumns.DURATION, VideoColumns._ID };
        int tocols[] = new int[] {android.R.id.text1, android.R.id.text2};
        Cursor c = managedQuery(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, columns, tocols);
        setListAdapter(sca);
     }
}
