package com.example.simpledialer;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.text.method.DialerKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Main extends Activity implements View.OnLongClickListener
{
	EditText mDigits;
	Button dialDelete;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //set the layout for the form
        setContentView(R.layout.main);
        
        //get the Delete button
        dialDelete = (Button)findViewById(R.id.dialDelete);
        
        //get the Digits Text Box
        mDigits = (EditText) findViewById(R.id.digits); 
        
        //attach a Dial Key Listener
        mDigits.setKeyListener(DialerKeyListener.getInstance());
        
        //Phone Number Text Formatter for the digits box
        mDigits.addTextChangedListener(new PhoneNumberFormattingTextWatcher());   
        
        //Add a Long Key Click listener
        dialDelete.setOnLongClickListener(this);
    }
    
    
	public void SendCall(View view) 
	{
		//get the phone number to send
		final String number = mDigits.getText().toString();
        if (number == null || !TextUtils.isGraphic(number)) {
            // There is no number entered.	            
            return;
        }
        
        //create intent for making a call
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", number, null));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        
        //call the intent
        startActivity(intent);        
        finish();			
	}
	
	public void DeleteNumber(View view)
	{		
		//create delete key event
		KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
		//pass the delete key event to the Digits Text Box
		mDigits.onKeyDown(KeyEvent.KEYCODE_DEL, event);
	}	

	public boolean onLongClick(View view) 
	{		
		 //find out which view was clicked
		 switch (view.getId())
	     {
	        case R.id.dialDelete: 
	        {
	        	 //get the Digits Text and clear it out
	        	mDigits.getText().clear();
	            return true;
	        }          
	     }
		return false;
	}
		
}