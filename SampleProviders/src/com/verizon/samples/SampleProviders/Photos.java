package com.verizon.samples.SampleProviders;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.ImageColumns;
import android.widget.SimpleCursorAdapter;

public class Photos extends ListActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        String columns[] = new String[] { ImageColumns.DISPLAY_NAME, ImageColumns.BUCKET_DISPLAY_NAME, ImageColumns._ID };
        int tocols[] = new int[] {android.R.id.text1, android.R.id.text2};
        Cursor c = managedQuery(Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, c, columns, tocols);
        setListAdapter(sca);
     }
}
