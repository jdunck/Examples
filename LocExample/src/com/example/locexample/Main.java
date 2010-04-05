package com.example.locexample;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

public class Main extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        TextView text1 = (TextView)findViewById(R.id.text1);
        
        Location loc = LocationUtility.GetLastKnownLocation(this);
        
        if(loc != null)
        	text1.setText(loc.toString());
    }
}