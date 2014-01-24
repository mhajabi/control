package com.example.control;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	 int rotation;
	 Display display;
	 Button tv,phone;
	 TextView info;
	 Dialog infoDialog;
	 Context con;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		con = this;
		 display = getWindowManager().getDefaultDisplay();
		rotation = display.getRotation();
		if(rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180){
			setContentView(R.layout.main_portrait);
			
		}
		else 
		setContentView(R.layout.main_landscape);
		tv = (Button) findViewById(R.id.btnTv);
		phone = (Button) findViewById(R.id.btnPhone);
		info = (TextView) findViewById(R.id.tvInfo);
		tv.setOnClickListener(click);
		phone.setOnClickListener(click);
		info.setOnClickListener(click);
		
	}
	android.view.View.OnClickListener click = new View.OnClickListener() {
		
	
		@Override
		public void onClick(View v) {
			
			if(v.getId() == R.id.btnPhone){
				Intent I = new Intent (MainActivity.this, PhoneActivity.class);
				startActivity(I);
			}
			else if(v.getId() == R.id.btnTv){
				Intent I = new Intent (MainActivity.this, TvActivity.class);
				startActivity(I);
				
			}
			else if(v.getId()== R.id.tvInfo){
				infoDialog = new Dialog(con);
				infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				infoDialog.setCancelable(true);
				infoDialog.setContentView(R.layout.turn_off);
				
		         //set up button
		         final Button turnOFF = (Button) infoDialog.findViewById(R.id.btnTurrnOFF);
		         final Button cancel = (Button) infoDialog.findViewById(R.id.btnNo);
		         final TextView message = (TextView) infoDialog.findViewById(R.id.tvMessage);
		         final TextView title = (TextView) infoDialog.findViewById(R.id.tvTitle);
		        	title.setText("Want to see the code!");
		        	message.setText("This requires Internet connection.");
		        
		         
		         turnOFF.setOnClickListener(new View.OnClickListener() {
		     
				@Override
				public void onClick(View v) {
					if(v.getId() == R.id.btnTurrnOFF){
					
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
		            	startActivity(browserIntent);
						Dismiss(infoDialog);
					}
				}
		         });
		         cancel.setOnClickListener(new View.OnClickListener() {
				     
						@Override
						public void onClick(View v) {
							if(v.getId() == R.id.btnNo){
						 
								Dismiss(infoDialog);
				 			
							}
						}
				         });
		         //now that the dialog is set up, it's time to show it    
		         infoDialog.show();
		         }
		}	
		
	};
	/*@Override
	protected void onPause(){}
	@Override
	protected void onStop(){}
	@Override
	protected void onRestart(){}
	@Override
	protected void onResume(){}
	@Override
	protected void onDestroy(){}
	*/
	

	/*@Override
	public void onConfigurationChanged(Configuration newConfig) {
		int or = newConfig.orientation;
	    // Checks the orientation of the screen
	    if (or == newConfig.ORIENTATION_PORTRAIT) {
	    //	setContentView(R.layout.main_landscape);
	        Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
	    } else if (or == newConfig.ORIENTATION_PORTRAIT){
	    	//setContentView(R.layout.main_portrait);
	    	Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
	    }
	}*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
void Dismiss(Dialog d){
	
	d.dismiss();
}
}
