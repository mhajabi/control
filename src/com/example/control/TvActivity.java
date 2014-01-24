package com.example.control;

import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.ConsumerIrManager;
import android.hardware.ConsumerIrManager.CarrierFrequencyRange;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@TargetApi(19)
public class TvActivity extends Activity {

	private MenuItem phone;
	private Button on,off;
	
	private Context con;
	private CarrierFrequencyRange[] arr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tv);
		on = (Button) findViewById(R.id.btnIrON);
		on.setOnClickListener(tvclicks);
		off = (Button) findViewById(R.id.btnIrOFF);
		off.setOnClickListener(tvclicks);

		con= this;

	try{
		 ConsumerIrManager ir;
		ir = (ConsumerIrManager) con.getSystemService(Context.CONSUMER_IR_SERVICE) ;
		Boolean b = ((ConsumerIrManager) ir).hasIrEmitter();
	if (b == false)
	{
		
		Toast info = Toast.makeText(
				TvActivity.this,"No ir emitter", Toast.LENGTH_LONG);
		info.show();
		android.util.Log.e("Control", "No ir emitter");
		
	}
	else{
		arr = ir.getCarrierFrequencies();
		on.setText(arr[0].toString());
	}
	}
	catch(Exception ex){
		android.util.Log.e("Control", " Problem checking IR ");
		android.util.Log.e("Control", ex.toString());
		
	}
	}

	
View.OnClickListener tvclicks = new View.OnClickListener() {
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnIrON ){
		try{
			//ir.transmit(0, null);
		}
		catch(Exception ex){
			android.util.Log.e("Control", " Problem sending on IR ");
			android.util.Log.e("Control", ex.toString());
		}	
		}
		else if (v.getId() == R.id.btnIrOFF){
			try{
			//	ir.transmit(0, null);
			}
			catch(Exception ex){
				android.util.Log.e("Control", " Problem sending on IR ");
				android.util.Log.e("Control", ex.toString());
			}	
		}
		
	}
};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tv, menu);
		phone = (MenuItem) findViewById(R.id.TV);
		return true;
	}
public boolean onOptionsItemSelected(MenuItem m) {
		
		if(m.getItemId() == R.id.PHONE ){
			 Intent I = new Intent(TvActivity.this, PhoneActivity.class);
			 startActivity(I);
			
		}
		
	return true;
}
}
