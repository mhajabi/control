package com.example.control;

import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class Bluetooth  {
	
	private BluetoothAdapter bta;
	private Context con;
	private Activity act ;
	private Dialog cancelDialog;
	
	
	
	Bluetooth(Activity activity){
		this.con = activity;
		this.bta = BluetoothAdapter.getDefaultAdapter();
		this.act = activity;
	}//ends the Constructor
	
	
	String  State() throws Exception{
		String s="";
		// set BlueTooth text----------------
		try{
				if (bta == null) {
					s = "Bluetooth not supported/not working.";
				}// ends if
				else {
					int bstate = bta.getState();

					switch (bstate) {
					case BluetoothAdapter.STATE_ON:
						s = "Turn Bluetooth OFF";
						break;
					case BluetoothAdapter.STATE_OFF:
						s=  "Turn Bluetooth ON";
						break;
					case BluetoothAdapter.STATE_TURNING_OFF:
		                s= "Turning Bluetooth off.";
		                break;
		           
		            case BluetoothAdapter.STATE_TURNING_ON:
		                s= "Turning Bluetooth on.";
		                break;
					}// ends switch
				}// ends else
					// ends BlueTooth changes ----------------
			}
		
			catch(Exception ex){
				 android.util.Log.e("Control", "problem in bluetooth State  ");
				 android.util.Log.e("Control", ex.toString());
				 throw ex;
			} 
		
		return s;
		}//ends state
	void Toggle() throws Exception{
		
		try {
			if (!bta.isEnabled()) {
				// if BlueTooth is disabled
				//bta.enable();
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			    act.startActivity(enableBtIntent);
		
			} 
			else {
				try{
					cancelDialog = new Dialog(con);
					cancelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					cancelDialog.setCancelable(true);
					cancelDialog.setContentView(R.layout.turn_off);
					
			         //set up button
			         final Button turnOFF = (Button) cancelDialog.findViewById(R.id.btnTurrnOFF);
			         final Button cancel = (Button) cancelDialog.findViewById(R.id.btnNo);
			         final TextView message = (TextView) cancelDialog.findViewById(R.id.tvMessage);
			         final TextView title = (TextView) cancelDialog.findViewById(R.id.tvTitle);
			        	title.setText("Want to turn Bluetooth OFF!");
			        	message.setText("This will stop any Bluetooth  operations.");
			        
			         
			         turnOFF.setOnClickListener(new View.OnClickListener() {
			     
					@Override
					public void onClick(View v) {
						if(v.getId() == R.id.btnTurrnOFF){
						
							bta.disable();
							Dismiss(cancelDialog);
						}
					}
			         });
			         cancel.setOnClickListener(new View.OnClickListener() {
					     
							@Override
							public void onClick(View v) {
								if(v.getId() == R.id.btnNo){
							 
									Dismiss(cancelDialog);
					 			
								}
							}
					         });
			         //now that the dialog is set up, it's time to show it    
			         cancelDialog.show();
					}
					catch(Exception ex){
						android.util.Log.e("Control", "problem in TurnOFF dialog ");
						android.util.Log.e("Control", ex.toString());
					}
				
				
			}// ends BlueTooth enabled else

		}// ends BlueTooth actions try
		catch (Exception ex) {
		 	 
			 throw ex;
		}// ends catch
	}// end Toggle
	public void Connect(String bname,String baddress){}
	public void disconnect(Button bluetoothConnection) {
		bta.cancelDiscovery();
		
		
		bluetoothConnection.setText("Pair");

	}// ends blueTooth connection
	public void scan() {
		Intent discoverableIntent = new
				Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				act.startActivity(discoverableIntent);
		//startDiscovery();
	}// ends scan
	public Boolean startDiscovery(){
		Boolean b = this.bta.startDiscovery();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return b;
	}
	public void stop(){
		this.bta.cancelDiscovery();
		
	}
	public String isconnected() throws Exception {
		try {
			ConnectivityManager connManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mBluetooth = connManager.getNetworkInfo(ConnectivityManager.TYPE_BLUETOOTH);
					

			if (mBluetooth.isConnected()) {

				return "Disconnect";
			} else {

				return "Pair";
			}
		} catch (Exception ex) {
			throw ex;
		}
	}// ends is connected
	private void Dismiss(Dialog d){
		d.dismiss();
	}
	public Set<BluetoothDevice> getBlueTooth(){
		Set<BluetoothDevice> set = bta.getBondedDevices();
		return set;
	}
}//ends class
