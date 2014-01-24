package com.example.control;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.widget.Button;
import android.widget.SeekBar;


public class Audio {
	
	private AudioManager am;
	private Context con;
Audio(Activity activity){
	this.con = activity;
	am = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
}//ends the constructor

void State(Button button, SeekBar seekbar,char layout) throws Exception{
// set the audio bar and text
	
	if(layout == 'p')return;
	try{
		if(am.getRingerMode() == AudioManager.RINGER_MODE_SILENT
		   || am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
		
		
		button.setText("Enable Ringer");
		seekbar.setVisibility(SeekBar.INVISIBLE);
	}
	else if(am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
		
		button.setText("Disable Ringer");
		seekbar.setVisibility(SeekBar.VISIBLE);
		seekbar.setMax(am.getStreamMaxVolume(AudioManager.STREAM_RING));
		seekbar.setProgress(am.getStreamVolume(AudioManager.STREAM_RING));
	}
}
catch(Exception ex){

	throw ex;
	
}
	
}//ends audio state
void Toggle(Button button,SeekBar seekbar){
	
	
	int value = seekbar.getMax();
	
	am = (AudioManager) con.getSystemService(Context.AUDIO_SERVICE);
	if (am.getRingerMode() == AudioManager.RINGER_MODE_SILENT
			|| am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {

		try {// if phone is on silent mode or vibrate
			seekbar.setVisibility(SeekBar.VISIBLE);
			button.setText("Disable Ringer");
			seekbar.setProgress(value);
			am.setStreamVolume(AudioManager.STREAM_RING,seekbar.getProgress(), 0);
		}// end audio button
		catch (Exception ex) {
			android.util.Log.e("Control", " problem in audio click");
			android.util.Log.e("Control", ex.toString());
		}

	}// end silent/vibrate mode change
	else if (am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
		seekbar.setVisibility(SeekBar.INVISIBLE);
		button.setText("Enable Ringer");
		
		seekbar.setProgress(0);
		am.setStreamVolume(AudioManager.STREAM_RING,seekbar.getProgress(), 0);
		}//ends the audio button actions

	
	}//ends Toggle
}//ends class
