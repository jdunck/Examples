package com.example.voicetext;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener{
	
	private static final int RECOGNIZE_SPEECH = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Button b = (Button)findViewById(R.id.button);
        b.setOnClickListener(this);
    }
    
    public void onClick(View target)
    {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
		startActivityForResult(i, RECOGNIZE_SPEECH);
    }
    
    @Override 
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	switch(requestCode)
    	{
    	case RECOGNIZE_SPEECH :
    		if(resultCode == RESULT_OK)
    		{
    			ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
    			
    			Intent i = new Intent(Intent.ACTION_SEND);
    	    	i.setType("text/plain");
    	    	i.putExtra(Intent.EXTRA_TEXT, results.get(0));
    	     	startActivity(i);
    	     	finish();
    		}
    	}
    }
}