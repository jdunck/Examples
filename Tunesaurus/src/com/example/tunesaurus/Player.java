package com.example.tunesaurus;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class Player extends Activity implements OnClickListener {
	
	MediaPlayer player = new MediaPlayer(); 
	ImageButton playpause;
	
	ServiceConnection conn; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		
		conn = new ServiceConnection() {
			public void onServiceConnected(ComponentName arg0, IBinder arg1) {
				// TODO Auto-generated method stub
			}

			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub				
			}
		}; 
		
		PlayerService p; 
		bindService(p, conn, Context.BIND_AUTO_CREATE);
		
		
		p.Start(); 
		p.Pause();
		
		
		TextView title = (TextView)findViewById(R.id.title);
		TextView artist = (TextView)findViewById(R.id.artist);
		playpause = (ImageButton)findViewById(R.id.playpause);
		
		Intent intent = getIntent();
		title.setText(intent.getStringExtra("title"));  
		artist.setText(intent.getStringExtra("artist"));
		playpause.setImageResource(android.R.drawable.ic_media_pause);
		
		playpause.setOnClickListener(this);
		
		try {
			player.reset();
			player.setDataSource(intent.getStringExtra("data"));
			player.prepare();
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onClick(View arg0) {
		if(player.isPlaying()) {
			player.pause();
			playpause.setImageResource(android.R.drawable.ic_media_play);
		} else {
			player.start();
			playpause.setImageResource(android.R.drawable.ic_media_pause);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		player.stop();
		Log.d("Tunesaurus", "destroying player");
	}

}
