package com.example.control;

import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.media.AudioManager;
import android.net.wifi.WifiManager;

public class PhoneActivity extends Activity {

	int rotation;
	Boolean dialogOpen,DiscoveryAttempt;
	Display display;
	MenuItem tv;
	Button wifi, bluetooth, audio, wifiState, bluetoothState;
	SeekBar audioSB;
	Context con;
	Activity activity;
	AudioManager aManager;
	char layout;
	Bluetooth b;
	Wifi w;
	Audio a;
	CustomDialog cd;
	Dialog scan;
	// Animation rotate ;
	IntentFilter filter;
	private HashMap<String, String> bList;
	// ImageView loading;
	
	private BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setting the layout with respect to device rotation-------

		// initializing needed components
		try {
			this.con = this;
			this.activity = this;
			this.display = getWindowManager().getDefaultDisplay();
			this.aManager = (AudioManager) con
					.getSystemService(Context.AUDIO_SERVICE);
			this.b = new Bluetooth(this);
			this.w = new Wifi(this);
			this.a = new Audio(this);
			this.cd = new CustomDialog(this);
			this.dialogOpen = false;
			this.bList = new HashMap<String, String>();
			// rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
			// loading = (ImageView) findViewById(R.id.ivLoading);

			
			this.filter = new IntentFilter();
			filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
			filter.addAction(BluetoothDevice.ACTION_FOUND);
			filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
			filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION);
			filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
			filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
			filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
			filter.addAction(BluetoothDevice.ACTION_FOUND);
			receiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
						try {
							bluetooth.setText(b.State());
							if (bluetooth.getText() == "Turn Bluetooth OFF") {
								bluetoothState.setVisibility(Button.VISIBLE);
							} else
								bluetoothState.setVisibility(Button.INVISIBLE);
							if (View.VISIBLE == bluetoothState.getVisibility()) {
								bluetoothState.setText(b.isconnected());
								try {
									cd.Dismiss(cd.dialog);
									
									cd.customOpen = false;
								} catch (Exception ex) {
								}
							}
						} catch (Exception ex) {
							android.util.Log.e("Control",
									"problem updating Bluetooth info");
							android.util.Log.e("Control", ex.toString());
						}
					else if (BluetoothDevice.ACTION_FOUND.equals(action)){
						 BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				            // Add the name and address to a hash
						 bList.put(device.getName(), device.getAddress());
						 android.util.Log
							.e("Control", device.getName()+":"+device.getAddress());
					}
					else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
						b.stop();
						if (dialogOpen == true) {
							dialogOpen = false;
							// loading.clearAnimation();
							scan.dismiss();
							try {
								
							
								Thread.sleep(1000);
								try{
									cd.show('b',bList);}
								catch(Exception ex){
									android.util.Log.e("Control",
											"problem in cd show bluetooth");
									android.util.Log.e("Control", ex.toString());
								}

							} catch (Exception ex) {
								android.util.Log.e("Control",
										"problem showing available Bluetooth Devices");
								android.util.Log.e("Control", ex.toString());
							}
						}

					else if (cd.customOpen == true) {
							try {

								cd.Dismiss(cd.dialog);
								cd.customOpen = false;
							
								Thread.sleep(1000);
								cd.show('b',bList);

							} catch (Exception ex) {
								android.util.Log.e("Control", "dismissing cd");
								android.util.Log.e("Control", ex.toString());
							}
					}
					}
					else if (WifiManager.WIFI_STATE_CHANGED_ACTION
							.equals(action)
							|| WifiManager.NETWORK_STATE_CHANGED_ACTION
									.equals(action)) {
					try{w.input.dismiss();}catch(Exception ex){
						
					}
						try {
							
							wifi.setText(w.State());
							if (wifi.getText() == "Turn WIFI OFF") {
								wifiState.setVisibility(Button.VISIBLE);
							} else
								wifiState.setVisibility(Button.INVISIBLE);
							if (View.VISIBLE == wifiState.getVisibility()) {
								wifiState.setText(w.isconnected());
								try {
									cd.Dismiss(cd.dialog);
									
									cd.customOpen = false;
								} catch (Exception ex) {
								}
							}
						} catch (Exception ex) {
							android.util.Log.e("Control",
									"problem updating WIFI info");
							android.util.Log.e("Control", ex.toString());
						}
					} else if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION
							.equals(action)) {
						
						if (dialogOpen == true) {
							dialogOpen = false;
							// loading.clearAnimation();
							scan.dismiss();
							try {
								
							//	cd.listDataW = w.getWiFi();
								Thread.sleep(1000);
								cd.show('w',null);

							} catch (Exception ex) {
								android.util.Log.e("Control",
										"problem showing available wifi");
								android.util.Log.e("Control", ex.toString());
							}
						}

					else if (cd.customOpen == true) {
							try {

								cd.Dismiss(cd.dialog);
								cd.customOpen = false;
							//	cd.listDataW = w.getWiFi();
								Thread.sleep(1000);
								cd.show('w',null);

							} catch (Exception ex) {
								android.util.Log.e("Control", "dismissing cd");
								android.util.Log.e("Control", ex.toString());
							}
					}

					} else if (AudioManager.RINGER_MODE_CHANGED_ACTION
							.equals(action) || AUDIO_SERVICE.equals(action)) {
						try {
							a.State(audio, audioSB, layout);
						} catch (Exception ex) {
							android.util.Log.e("Control",
									"problem updating Audio info");
							android.util.Log.e("Control", ex.toString());
						}
					}
				}
			};
			registerReceiver(receiver, filter);
		} catch (Exception ex) {
			android.util.Log.e("Control", "problem initializing components");
			android.util.Log.e("Control", ex.toString());
		}

		try {
			layout = SetLayout(display, aManager);
			setText(layout, b, w, a);
		} catch (Exception ex) {
			android.util.Log.e("Control", " problem setting layout/text");
			android.util.Log.e("Control", ex.toString());
		}
	}// ends OnCreate

	// connecting buttons to OnClick
	OnClickListener click = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(v.getId() == R.id.btnBluetoothState){
				try{
				if(bluetoothState.getText() == "Disconnect")
					b.disconnect(bluetoothState);
				else if (bluetoothState.getText() == "Pair"){
					bList = new HashMap<String, String>();
					b.scan();
					DiscoveryAttempt = b.startDiscovery();
					scan = new Dialog(con);
					scan.requestWindowFeature(Window.FEATURE_NO_TITLE);
					scan.setContentView(R.layout.scanning);
					final Window window = scan.getWindow();
					window.setLayout(
							WindowManager.LayoutParams.MATCH_PARENT,
							WindowManager.LayoutParams.MATCH_PARENT);
					window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
					window.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT));
					dialogOpen = true;
					scan.show();
					
				}
			}catch(Exception ex){
				
				android.util.Log.e("Control", " problem in BluetoothState click");
		android.util.Log.e("Control", ex.toString());
			}
			} 	
			
			
		else if (v.getId() == R.id.btnWifiState) {
				try {
					
					if (wifiState.getText() == "Disconnect")
						w.disconnect(wifiState);
					else if (wifiState.getText() == "Connect") {
						w.scan();
						scan = new Dialog(con);
						scan.requestWindowFeature(Window.FEATURE_NO_TITLE);
						scan.setContentView(R.layout.scanning);
						final Window window = scan.getWindow();
						window.setLayout(
								WindowManager.LayoutParams.MATCH_PARENT,
								WindowManager.LayoutParams.MATCH_PARENT);
						window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
						window.setBackgroundDrawable(new ColorDrawable(
								Color.TRANSPARENT));
						dialogOpen = true;
						scan.show();
						// loading.startAnimation(rotate);

					}
				} catch (Exception ex) {
					android.util.Log
							.e("Control", " problem in wifiState click");
					android.util.Log.e("Control", ex.toString());
				}

			}

			if (v.getId() == R.id.btnBluetooth) {
				// if user clicked on BlueTooth button
				try {
					b.Toggle();
				} catch (Exception ex) {
					android.util.Log
							.e("Control", " problem in bluetooth click");
					android.util.Log.e("Control", ex.toString());
				}

			}// end BluTooth button actions
			else if (v.getId() == R.id.btnWifi) {
				try {
					w.Toggle();
				} catch (Exception ex) {
					android.util.Log.e("Control", " in wifi click");
					android.util.Log.e("Control", ex.toString());
				}
			}// ends WiFi button actions
			else if (v.getId() == R.id.btnAudio) {
				a.Toggle(audio, audioSB);

			}
		}// ends onClick
	};// ends click

	/*
	 * @Override protected void onPause(){}
	 * 
	 * @Override protected void onStop(){}
	 * 
	 * @Override protected void onDestroy(){}
	 */

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		// getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
		super.onDestroy();
	}

	@Override
	public void onResume() {

		setText(layout, b, w, a);
		super.onResume();
	}

	@Override
	public void onRestart() {

		setText(layout, b, w, a);
		super.onRestart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.phone, menu);
		tv = (MenuItem) findViewById(R.id.TV);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem m) {

		// change to TV activity if option clicked
		if (m.getItemId() == R.id.TV) {
			Intent I = new Intent(PhoneActivity.this, TvActivity.class);
			startActivity(I);

		}

		return true;
	}

	public char SetLayout(Display display, AudioManager am) {
		char layout;
		rotation = display.getRotation();
		if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
			// device in portrait rotation
			layout = 'p';
			// set portrait layout
			setContentView(R.layout.phone_portrait);

			Toast info = Toast.makeText(PhoneActivity.this,
					"Rotate phone for more options", Toast.LENGTH_SHORT);
			info.show();
		}// ends the portrait rotation actions
		else {
			// device in landscape rotation
			layout = 'l';
			setContentView(R.layout.phone_landscape);
			audio = (Button) findViewById(R.id.btnAudio);
			audioSB = (SeekBar) findViewById(R.id.sbAudio);
			audio.setOnClickListener(click);
			try {
				a.State(audio, audioSB, layout);
			} catch (Exception ex) {
				android.util.Log.e("Control", "problem updating audio info");
				android.util.Log.e("Control", ex.toString());
			}

			audioSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				public void onProgressChanged(SeekBar seekBar, int progress,
						boolean fromTouch) {

					if (seekBar.getId() == R.id.sbAudio) {
						// changes volume if audio is changed
						int value = audioSB.getProgress();
						if (value == 0)
							audio.setText("Enable Ringer");
						aManager.setStreamVolume(AudioManager.STREAM_RING,
								value, 0);
					}
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				}
			});
		}// ends the landscape rotation actions

		// connect shared layout elements

		wifi = (Button) findViewById(R.id.btnWifi);
		bluetooth = (Button) findViewById(R.id.btnBluetooth);
		wifiState = (Button) findViewById(R.id.btnWifiState);
		bluetoothState = (Button) findViewById(R.id.btnBluetoothState);
		bluetooth.setOnClickListener(click);
		wifi.setOnClickListener(click);
		wifiState.setOnClickListener(click);
		bluetoothState.setOnClickListener(click);
		return layout;
	}

	public void setText(char layout, Bluetooth bt, Wifi wi, Audio a) {

		// set BlueTooth text----------------
		try {
			bluetooth.setText(bt.State());
			if (bluetooth.getText() == "Turn Bluetooth OFF")
				bluetoothState.setVisibility(Button.VISIBLE);
			else
				bluetoothState.setVisibility(Button.INVISIBLE);
		} catch (Exception ex) {
			android.util.Log.e("Control", "problem updating Bluetooth info");
			android.util.Log.e("Control", ex.toString());
		}
		// ends BlueTooth changes ----------------

		// set WIFI text ----------------------
		try {
			wifi.setText(wi.State());
			if (wifi.getText() == "Turn WIFI OFF")
				wifiState.setVisibility(Button.VISIBLE);
			else
				wifiState.setVisibility(Button.INVISIBLE);
		} catch (Exception ex) {
			android.util.Log.e("Control", "problem updating WIFI info");
			android.util.Log.e("Control", ex.toString());
		}

		// ends WIFI changes -----------------

		// set seek bars values & audio button if layout landscape-----------
		if (layout == 'l') {
			try {
				a.State(audio, audioSB, layout);
			} catch (Exception ex) {
				android.util.Log.e("Control", "problem updating Audio info");
				android.util.Log.e("Control", ex.toString());
			}// ends seek bars changes ----------------
		}
	}// ends updateText

}// ends PhoneActivity

