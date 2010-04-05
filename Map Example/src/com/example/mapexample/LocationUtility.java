package com.example.mapexample;

import java.util.List;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtility 
{
	public static Location GetLastKnownLocation(Context context)
	{
		// you might be tempted to use LocationManager.getBestProvider here instead
		// but beware, it is less likely to return a location than this strategy
		
		Location location = null, temp = null;
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = manager.getAllProviders(); 
        boolean enabled = false;

        // loop through location providers
        for(String provider : providers) {
        	
        	// if we've found an active provider and this one isn't, skip it
        	if (enabled && !manager.isProviderEnabled(provider)) 
        		continue;
        	
        	// get the location from the provider
        	temp = manager.getLastKnownLocation(provider);
        	
        	// if it's more accurate than what we've got, replace
        	if (temp != null) {
        		if (location == null || location.getAccuracy() < temp.getAccuracy()) {
        			location = temp;
        			enabled = manager.isProviderEnabled(provider);
        		}
        	}
        }
        return location; 
 	}
}
