package com.example.wifiscanner;

import java.util.HashMap;
import java.util.List;

import java.io.*;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.DialogInterface;
import android.content.DialogInterface.*;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.provider.Settings.Secure;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.provider.Settings;
import android.provider.Settings.Secure;

public class WifiScanner extends Activity implements OnClickListener {
	private static final String TAG = "WifiScanner";
	// public static final String userID =
	// android.provider.Settings.Secure.ANDROID_ID;
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
	public void onPause() {
		// unregisterReceiver(receiver);
		unregisterForContextMenu(textStatus);
		unregisterForContextMenu(buttonScan);
	}

	@Override
	public void onStop() {
		// unregisterReceiver(receiver);
		unregisterForContextMenu(textStatus);
		unregisterForContextMenu(buttonScan);
	}

	@Override
	public void onDestroy() {
		unregisterForContextMenu(textStatus);
		unregisterForContextMenu(buttonScan);
	}

	public void onClick(View view) {

		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			Toast.makeText(this, "External SD card not mounted",
					Toast.LENGTH_LONG).show();
		}
		textStatus = (TextView) findViewById(R.id.textStatus);
		textStatus.setText("");
		Toast.makeText(this, "Start Scan now!!", Toast.LENGTH_LONG).show();
		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");

		}
		wifi.startScan();
		try {
			Thread.sleep(700);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		ArrayList<List<ScanResult>> ScanList = new ArrayList<List<ScanResult>>(
				5);
		for (int scancount = 0; scancount < 5; scancount++) {
			wifi.startScan();
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<ScanResult> results = this.wifi.getScanResults();
			ScanList.add(results);
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

			Log.d(TAG, "onReceive() message: " + message);
		}

		HashMap<String, Double> map_sig = new HashMap<String, Double>();
		HashMap<String, Double> map_num = new HashMap<String, Double>();
		// TODO Compare 5 scan results and Calculate an average value
		// List<ScanResult> average;
		for (int i = 0; i < 5; i++) {
			List<ScanResult> res = ScanList.get(i);
			for (int j = 0; j < res.size(); j++) {
				if (!map_sig.containsKey(res.get(j).BSSID)) {
					map_sig.put(res.get(j).BSSID,
							Double.valueOf(res.get(j).level));
					map_num.put(res.get(j).BSSID, Double.valueOf(1));
				} else {
					map_sig.put(res.get(j).BSSID,
							(Double.valueOf(map_sig.get(res.get(j).BSSID))
									.doubleValue() + res.get(j).level));
					map_num.put(res.get(j).BSSID,
							(Double.valueOf(map_num.get(res.get(j).BSSID))
									.doubleValue() + 1));
				}
			}
		}
		HashMap<String, Double> map_avg = new HashMap<String, Double>();
		for (String key : map_sig.keySet()) {
			if (Double.valueOf(map_num.get(key)) >= 3)
				map_avg.put(
						key,
						Double.valueOf(map_sig.get(key).doubleValue()
								/ map_num.get(key).doubleValue()));
			textStatus.append(key + ":" + map_avg.get(key).doubleValue() + " "
					+ map_num.get(key).doubleValue() + "\n");
		}

		// create a xml formatted string
		String xml;
		String header = "<?xml version=\"1.0\"?>";
		String session = "<session><number>" + map_avg.size() + "</number>";
		String content = "<content>";
		int i = 1;
		for (String key : map_avg.keySet()) {
			content += "<";
			content += i;
			content += ">";
			i++;
		}
		xml = header + session + content;
		textStatus.append(xml);

		/* get IMEI */
		// TelephonyManager telephonyManager =
		// (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		// textStatus.append(telephonyManager.getDeviceId());

		/* get Android ID */
		// String Id = Settings.Secure.getString(getContentResolver(),
		// Settings.Secure.ANDROID_ID);
		// textStatus.append(Id);

		// try {
		// HttpClient httpclient = new DefaultHttpClient();
		// HttpPost httppost = new HttpPost(
		// "http://inpoint.pdp.fi/wlan/wlan.php");
		// // StringEntity se = new StringEntity(xml, HTTP.UTF_8);
		// // se.setContentType("text/xml");
		// // httppost.setHeader("Content-Type",
		// // "application/soap+xml;charset=UTF-8");
		// // httppost.setEntity(se);
		//
		// /* send AP info as a form to the server through http post */
		// List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		// // Your DATA
		// for (int i = 0; i < 5; i++) {
		// List<ScanResult> res = ScanList.get(i);
		// for (int j = 0; j < res.size(); j++) {
		// // nameValuePairs.add(new
		// // BasicNameValuePair(res.get(j).BSSID,
		// // String.valueOf(res.get(j).level)));
		// nameValuePairs.add(new BasicNameValuePair("MAC",
		// res.get(j).BSSID));
		// nameValuePairs.add(new BasicNameValuePair("SIG", String
		// .valueOf(res.get(j).level)));
		// }
		// }
		// // nameValuePairs.add(new BasicNameValuePair("id", "12345"));
		// // nameValuePairs.add(new
		// // BasicNameValuePair("stringdata","AndDev is Cool!"));
		//
		// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		//
		// BasicHttpResponse httpResponse = (BasicHttpResponse) httpclient
		// .execute(httppost);
		//
		// // tested for http get request, now works
		// // HttpGet getRequest = new HttpGet(
		// // "http://inpoint.pdp.fi/wlan/wlan.php");
		// // HttpResponse response = httpclient.execute(getRequest);
		//
		// // read echo from server
		// BufferedReader reader = new BufferedReader(new InputStreamReader(
		// httpResponse.getEntity().getContent(), "UTF-8"));
		// String json = reader.readLine();
		//
		// textStatus.append(json);
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// } catch (ClientProtocolException e) {
		// e.printStackTrace();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
	}
}