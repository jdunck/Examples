package com.example.Tweetastic;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {
	
	TextView tvusername; 
	TextView tvpassword;
	TextView tvstatus; 

	@Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        tvusername = (TextView)findViewById(R.id.username);
        tvpassword = (TextView)findViewById(R.id.password);
        tvstatus = (TextView)findViewById(R.id.status);
        
        Intent starter = getIntent();
        if(Intent.ACTION_SEND.equals(starter.getAction()))
        {
        	tvstatus.setText(starter.getStringExtra(Intent.EXTRA_TEXT));
        }
    }
	
    public void clickHandler(View target)
    {
        String username = tvusername.getText().toString();
        String password = tvpassword.getText().toString();
        String status = tvstatus.getText().toString();
        
        HttpPoster poster = new HttpPoster("https://twitter.com/statuses/update.xml");
        String creds = username + ":" + password;
        poster.AddHeader("Authorization", "Basic " + new String(Base64.encodeBase64(creds.getBytes())));
        try {
			poster.AddValue("status", status);
			poster.doPost();
			Toast.makeText(Main.this, "status updated", Toast.LENGTH_LONG).show();		
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(Main.this, "update failed!", Toast.LENGTH_LONG).show();		
		}	
    }
}