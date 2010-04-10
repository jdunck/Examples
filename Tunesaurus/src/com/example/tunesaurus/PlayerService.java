package com.example.tunesaurus;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayerService extends Service {
	
	private MediaPlayer player; 

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	
	public void Start() {
		player.start(); 
	}
	
	public void Pause() {
		player.pause();
	}

}
