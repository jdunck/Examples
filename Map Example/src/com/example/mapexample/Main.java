package com.example.mapexample;

import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

public class Main extends MapActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); 
        
        // get your MD5 hash of the debug.keystore with this
        // keytool -list -keystore debug.keystore
        // the password is "android"
        // debug keystore is in the .android folder Documents and Settings\ username \.android on xp
        
        // use the MD5 to get an Android Maps API key here: 
        // http://code.google.com/intl/pl/android/maps-api-signup.html
        
        MapView mapview = (MapView)findViewById(R.id.mapview);
        
        // this method is deprecated but google's tutorial isn't updated
        // linearlayout.addView(mapview.getZoomControls());
        
        // use this instead 
        mapview.setBuiltInZoomControls(true);
    }

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}