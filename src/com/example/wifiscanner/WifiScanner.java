package com.example.wifiscanner;

import java.util.List;

import java.io.*;
import java.io.BufferedWriter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class WifiScanner extends Activity implements OnClickListener {
	private static final String TAG = "WifiScanner";
	WifiManager wifi;
	// BroadcastReceiver receiver;

	TextView textStatus;
	Button buttonScan;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Setup UI
		textStatus = (TextView) findViewById(R.id.textStatus);
		buttonScan = (Button) findViewById(R.id.buttonScan);
		buttonScan.setOnClickListener(this);

		// Setup WiFi
		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

		// Get WiFi status
		/*
		 * WifiInfo info = wifi.getConnectionInfo();
		 * textStatus.append("\n\nWiFi Status: " + info.toString());
		 */

		// List available networks
		/*
		 * List<WifiConfiguration> configs = wifi.getConfiguredNetworks(); for
		 * (WifiConfiguration config : configs) { textStatus.append("\n\n" +
		 * config.toString()); }
		 */

		textStatus.append("Start scanning and logging..\n");
		// registerReceiver(receiver, new IntentFilter(
		// WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		Log.d(TAG, "onCreate()");
	}

	@Override
	public void onStop() {
		// unregisterReceiver(receiver);
	}

	public void onClick(View view) {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			Toast.makeText(this, "External SD card not mounted",
					Toast.LENGTH_LONG).show();
		}

		textStatus = (TextView) findViewById(R.id.textStatus);
		Toast.makeText(this, "Start Scan now!!", Toast.LENGTH_LONG).show();
		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");
			wifi.startScan();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		for (int scancount = 0; scancount < 5; scancount++) {
			List<ScanResult> results = this.wifi.getScanResults();
			ScanResult bestSignal = null;
			for (ScanResult result : results) {
				if (bestSignal == null
						|| WifiManager.compareSignalLevel(bestSignal.level,
								result.level) < 0)
					bestSignal = result;
			}

			String message = String.format(
					"%d round scan: %s networks found. %s is the strongest.",
					scancount, results.size(), bestSignal.SSID);
			textStatus.append(message + '\n');
			 try {
			 File myFile = new File(Environment
			 .getExternalStorageDirectory().getPath()
			 + "/Inpoint_ScanResult.txt");
			 if (!myFile.exists())
			 myFile.createNewFile();
			 BufferedWriter bW;
			 bW = new BufferedWriter(new FileWriter(myFile, true));
			 bW.write(message);
			 bW.newLine();
			 bW.flush();
			 bW.close();
			
			 } catch (Exception e) {
			 Toast.makeText(getBaseContext(), e.getMessage(),
			 Toast.LENGTH_SHORT).show();
			 }

			Toast.makeText(this, message, Toast.LENGTH_LONG).show();
			for (int i = 0; i < results.size(); i++) {
				String message1 = String
						.format("AP num: %d\nessid: %s \nMAC: %s\nfrequency: %s\nSig: %d",
								i + 1, results.get(i).SSID,
								results.get(i).BSSID, results.get(i).frequency,
								results.get(i).level);
				// Toast.makeText(this, message1, Toast.LENGTH_LONG).show();
				textStatus.append(message1 + '\n');
				 try {
				 File myFile = new File(Environment
				 .getExternalStorageDirectory().getPath()
				 + "/Inpoint_ScanResult.txt");
				 if (!myFile.exists())
				 myFile.createNewFile();
				 BufferedWriter bW;
				 bW = new BufferedWriter(new FileWriter(myFile, true));
				 bW.write(message1);
				 bW.newLine();
				 bW.flush();
				 bW.close();
				
				 } catch (Exception e) {
				 Toast.makeText(getBaseContext(), e.getMessage(),
				 Toast.LENGTH_SHORT).show();
				 }
			}
			
			// TODO Compare 5 scan results and Calculate an average value

			Log.d(TAG, "onReceive() message: " + message);
		}
	}
}