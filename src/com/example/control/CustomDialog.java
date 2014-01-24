package com.example.control;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.ScanResult;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class CustomDialog {

	private Context con;
	public Dialog dialog;
	public Boolean customOpen = false;
	private ListView list;
	private String[] ssid;
	private String[] bNames;
	private String[] bAddress;
	private String[] bssid;
	private String[] cap;
	private int[] arr;
	private Wifi w;
	private Bluetooth b;
	private Activity act;
	private List<ScanResult> listDataW;
	private Set<BluetoothDevice> setDatab;
	private HashMap<String, String> resultlistW;
	private HashMap<String, String> resultlistB;
	private HashMap<String, Integer> resultlvl;
	private HashMap<String, String> capResults;

	CustomDialog(Activity activity) throws Exception {
		this.con = activity;
		this.act = activity;
		this.w = new Wifi(act);
		this.b = new Bluetooth(act);
		resultlistW = new HashMap<String, String>();
		resultlvl = new HashMap<String, Integer>();
		capResults = new HashMap<String, String>();
		resultlistB =  new HashMap<String, String>();

	}

	public void show(char connection,HashMap<String, String> bList) throws Exception {
		try {
			if (connection == 'w') {
				// bluetooth hash not need make null
				resultlistB= null;

				listDataW = w.getWiFi();

				for (ScanResult result : listDataW) {

					resultlistW.put(result.SSID, result.BSSID);
					resultlvl.put(result.SSID, result.level);
					capResults.put(result.SSID, result.capabilities);

				}
				ssid = resultlistW.keySet().toArray(
						new String[resultlistW.size()]);
				arr = new int[ssid.length];
				bssid = new String[ssid.length];
				cap = new String[ssid.length];
				for (int i = 0; i < arr.length; i++) {
					arr[i] = resultlvl.get(ssid[i]);
					cap[i] = capResults.get(ssid[i]);
					bssid[i] = resultlistW.get(ssid[i]);

				}

				dialog = new Dialog(con);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.elements_view);
				list = (ListView) dialog.findViewById(R.id.lvElements);
				CustomArrayAdapter ad = new CustomArrayAdapter(con,
						R.layout.element_layout, ssid, arr, cap);
				list.setAdapter(ad);
				list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int position, long arg3) {

						String SSID = ssid[position];
						String BSSID = bssid[position];
						String CAP = cap[position];

						try {

							w = new Wifi(act);

							w.Connect(SSID, BSSID, CAP);
							customOpen = false;
							// Dismiss(dialog);
						} catch (Exception ex) {
							android.util.Log
									.e("Control", "configuiring wifi  ");
							android.util.Log.e("Control", ex.toString());
						}

					}

				});

				final Window window = dialog.getWindow();
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
						WindowManager.LayoutParams.MATCH_PARENT);
				window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

				window.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				customOpen = true;
				dialog.show();
			}
			else if(connection == 'b'){
				Thread.sleep(10000);
				resultlistB = bList;
				//setDatab = b.getBlueTooth();
				
				/*for (BluetoothDevice result : setDatab) {
					
					resultlistB.put(result.getName(), result.getAddress());
					String s = result.getName();
					Toast info = Toast.makeText(con,
							""+s, Toast.LENGTH_LONG);
					info.show();
					android.util.Log.e("Control",
							"problem in cd show bluetooth");
					android.util.Log.e("Control", result.getName());
				}*/
					bNames = resultlistB.keySet().toArray(
							new String[resultlistB.size()]);
					bAddress = new String[bNames.length];
					try{
					for (int i = 0; i < bAddress.length; i++) {
						
						bAddress[i] = resultlistB.get(bNames[i]);
						android.util.Log
						.e("Control", bNames[i]+":"+bAddress[i]);
					}
					}catch(Exception ex){
						
				android.util.Log.e("Control", ex.toString());
					}
					dialog = new Dialog(con);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.elements_view);
					list = (ListView) dialog.findViewById(R.id.lvElements);
					CustomArrayAdapter ad = new CustomArrayAdapter(con,
							R.layout.element_layout, bNames,bAddress);
					list.setAdapter(ad);
					list.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View v,
								int position, long arg3) {

							String BNAME = bNames[position];
							String BADDRESS = bAddress[position];
							

							try {

								b = new Bluetooth(act);

								b.Connect(BNAME, BADDRESS);
								customOpen = false;
								// Dismiss(dialog);
							} catch (Exception ex) {
								android.util.Log
										.e("Control", "configuiring wifi  ");
								android.util.Log.e("Control", ex.toString());
							}

						}

					});

					final Window window = dialog.getWindow();
					window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
							WindowManager.LayoutParams.MATCH_PARENT);
				//	window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

					window.setBackgroundDrawable(new ColorDrawable(
							Color.TRANSPARENT));
					customOpen = true;
					dialog.show();

				
				
			}

		} catch (Exception ex) {
			throw ex;

		}

	}// ends the show

	public void Dismiss(Dialog d) {
		d.dismiss();

	}
}// ends the class
