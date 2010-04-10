package com.example.locexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Main extends Activity{

	Location loc; 
	TextView text1, text2;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        text1 = (TextView)findViewById(R.id.text1);
        text2 = (TextView)findViewById(R.id.text2); 
        
        loc = LocationUtility.GetLastKnownLocation(this);
        
        if(loc != null)
        	text1.setText(loc.toString());
    }
    
	public void lookupClick(View v) 
	throws IOException, JSONException {
		String urlstring = String.format("http://maps.google.com/maps/api/geocode/json?latlng=%s,%s&sensor=true", loc.getLatitude(), loc.getLongitude());
		Log.d("example", urlstring);
		URL url = new URL(urlstring);
		URLConnection con = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		StringBuffer out = new StringBuffer();
		while ((line = in.readLine()) != null) { out.append(line); }
		in.close();
		
		JSONObject obj = new JSONObject(out.toString());
		String addy = obj.getJSONArray("results").getJSONObject(0).getString("formatted_address");
		text2.setText(addy);
	}
}