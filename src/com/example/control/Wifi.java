package com.example.control;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Wifi {

	private WifiManager wm;
	private Context con;
	private Activity act;
	public Dialog input;
	private Dialog cancelDialog;
	private String passString;
	Boolean configured = false;

	Wifi(Activity activity) {
		this.con = activity;
		this.act = activity;
		wm = (WifiManager) con.getSystemService(Context.WIFI_SERVICE);
	}// ends the Constructor

	String State() throws Exception {
		String s = "";
		// set BlueTooth text----------------
		try {
			if (wm == null) {
				s = "WIFI not supported/not working.";
			}// ends if
			else {
				int wstate = wm.getWifiState();

				switch (wstate) {
				case WifiManager.WIFI_STATE_ENABLED:
					s = "Turn WIFI OFF";
					break;
				case WifiManager.WIFI_STATE_DISABLED:
					s = "Turn WIFI ON";
					break;
				case WifiManager.WIFI_STATE_DISABLING:
					s = "Turning WIFI off.";
					break;

				case WifiManager.WIFI_STATE_ENABLING:
					s = "Turning WIFI on.";
					break;
				}// ends switch
			}// ends else
				// ends BlueTooth changes ----------------
		}

		catch (Exception ex) {
			android.util.Log.e("Control", "problem in wifi State ");
			android.util.Log.e("Control", ex.toString());
			throw ex;

		}

		return s;
	}// ends state

	void Toggle() throws Exception {

		try {
			if (!wm.isWifiEnabled()) {
				// if WiFi is disabled
				wm.setWifiEnabled(true);
			}// ends WiFi disabled if
			else {
				try {
					cancelDialog = new Dialog(con);
					cancelDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					cancelDialog.setCancelable(true);
					cancelDialog.setContentView(R.layout.turn_off);

					// set up button
					final Button turnOFF = (Button) cancelDialog
							.findViewById(R.id.btnTurrnOFF);
					final Button cancel = (Button) cancelDialog
							.findViewById(R.id.btnNo);
					final TextView message = (TextView) cancelDialog
							.findViewById(R.id.tvMessage);
					final TextView title = (TextView) cancelDialog
							.findViewById(R.id.tvTitle);
					title.setText("Want to turn WIFI OFF!");
					message.setText("This will stop any WIFI  operations.");

					turnOFF.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (v.getId() == R.id.btnTurrnOFF) {

								wm.setWifiEnabled(false);
								Dismiss(cancelDialog);
							}
						}
					});
					cancel.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (v.getId() == R.id.btnNo) {

								Dismiss(cancelDialog);

							}
						}
					});
					// now that the dialog is set up, it's time to show it
					cancelDialog.show();
				} catch (Exception ex) {
					android.util.Log.e("Control", "problem in TurnOFF dialog ");
					android.util.Log.e("Control", ex.toString());
				}

			}// ends WiFi disabled else
		} catch (Exception ex) {
			android.util.Log.e("Control", "problem in wifi Toggle ");
			android.util.Log.e("Control", ex.toString());
			throw ex;
		}
	}// end Toggle

	public void disconnect(Button wificonnection) {
		wm.disconnect();
		wificonnection.setText("Connect");

	}// ends WiFi connection

	public void scan() {
		this.wm.startScan();
	}// ends scan

	String isconnected() throws Exception {
		try {
			ConnectivityManager connManager = (ConnectivityManager) con
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mWifi.isConnected()) {

				return "Disconnect";
			} else {

				return "Connect";
			}
		} catch (Exception ex) {
			throw ex;
		}
	}// ends is connected

	public void Connect(final String ssid, String bssid, final String cap)
			throws Exception {
		try {
			// at the moment bssid is passed for no reason, just in case of
			// future need
			wm.disconnect();
			configured = false;
			// A:here i want to make the network i disconnect from has info
			// saved
			List<WifiConfiguration> configlist = wm.getConfiguredNetworks();

			configured = connectConfigured(configlist, configured, ssid);

			// wifi not configured ,configuring here.
			if (configured == false) {

				if (cap.contains("WPA") || cap.contains("WEP")
						|| cap.contains("PSK") || cap.contains("EAP")) {
					// the wifi is secured ask for password
					input = new Dialog(con);
					input.requestWindowFeature(Window.FEATURE_NO_TITLE);
					input.setCancelable(true);
					input.setContentView(R.layout.password);

					// set up button
					final Button ok = (Button) input.findViewById(R.id.btnOk);
					final Button cancel = (Button) input
							.findViewById(R.id.btnCancel);
					final EditText pass = (EditText) input
							.findViewById(R.id.etPass);
					// pass.setPrivateImeOptions("");
					pass.setText("");

					ok.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (v.getId() == R.id.btnOk) {
								passString = pass.getText().toString();
								connectNotConfigured(passString, ssid, cap);
								try {
									Thread.sleep(3000);
									String s = isconnected();
									if (s == "Disconnect")
										Dismiss(input);

								} catch (Exception ex) {

								}

							}
						}
					});
					cancel.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (v.getId() == R.id.btnCancel) {

								Dismiss(input);

							}
						}
					});
					// now that the dialog is set up, it's time to show it
					input.show();

				} else {
					// if wifi is not secured

					WifiConfiguration wc = new WifiConfiguration();
					wc.SSID = "\"" + ssid + "\"";

					wc.status = WifiConfiguration.Status.ENABLED;
					wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);

					// connect to and enable the connection
					int netId = wm.addNetwork(wc);
					wm.enableNetwork(netId, true);
					wm.setWifiEnabled(true);

				}

			}

		} catch (Exception ex) {
			throw ex;

		}
	}// ends ConnectWiFi

	private Boolean connectConfigured(List<WifiConfiguration> configlist,
			Boolean configured, String ssid) {
		for (WifiConfiguration i : configlist) {
			if (i.SSID != null && i.SSID.equals("\"" + ssid + "\"")) {
				wm.disconnect();
				wm.enableNetwork(i.networkId, true);
				wm.reconnect();
				configured = true;
				// break;
			}

		}
		return configured;

	}

	private void connectNotConfigured(String pass, String ssid, String cap) {

		if (cap.contains("WPA") || cap.contains("WPA2")) {
			WifiConfiguration wc = new WifiConfiguration();
			wc.SSID = "\"" + ssid + "\"";
			wc.preSharedKey = "\"" + pass + "\"";
			wc.status = WifiConfiguration.Status.ENABLED;
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.TKIP);
			wc.allowedPairwiseCiphers
					.set(WifiConfiguration.PairwiseCipher.CCMP);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			// connect to and enable the connection
			int netId = wm.addNetwork(wc);
			wm.enableNetwork(netId, true);
			wm.setWifiEnabled(true);
		} else if (cap.contains("WEP")) {
			WifiConfiguration wc = new WifiConfiguration();
			wc.SSID = "\"" + ssid + "\"";
			wc.wepTxKeyIndex = 0;
			wc.wepKeys[0] = "\"" + pass + "\"";
			wc.status = WifiConfiguration.Status.ENABLED;
			wc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
			wc.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
			wc.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
			wc.allowedAuthAlgorithms
					.set(WifiConfiguration.AuthAlgorithm.SHARED);

			// connect to and enable the connection
			int netId = wm.addNetwork(wc);
			wm.enableNetwork(netId, true);
			wm.setWifiEnabled(true);

		}

	}

	private void Dismiss(Dialog d) {
		d.dismiss();
	}

	public List<ScanResult> getWiFi() {

		List<ScanResult> result = wm.getScanResults();
		return result;

	}// ends getWiFi

}// ends class
