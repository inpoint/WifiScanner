package com.example.wifiscanner;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import android.provider.Settings.Secure;

// Save data into XML//
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlSerializer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;
import android.widget.TextView;

//////////////////////



public class WifiScanner extends Activity implements OnClickListener {
	private static final String TAG = "WifiScanner";
//	public static final String userID = android.provider.Settings.Secure.ANDROID_ID;
	WifiManager wifi;
	// BroadcastReceiver receiver;

	TextView textStatus;
	Button buttonScan;
	String UserID = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
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

        //create a new file called "new.xml" in the SD card
        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/new.xml");
        try{
                newxmlfile.createNewFile();
        }catch(IOException e){
                Log.e("IOException", "exception in createNewFile() method");
        }	
        //we have to bind the new file with a FileOutputStream
        FileOutputStream fileos = null;         
        try{
                fileos = new FileOutputStream(newxmlfile);
        }catch(FileNotFoundException e){
                Log.e("FileNotFoundException", "can't create FileOutputStream");
        }
        
        //scan the wifi and save the result
		Toast.makeText(this, "Start Scan now!!", Toast.LENGTH_LONG).show();
		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");

		}
        ArrayList<List<ScanResult>> ScanList = new ArrayList<List<ScanResult>>(5);
        for (int scanCnt = 0; scanCnt < 6; scanCnt++)
        {
			wifi.startScan();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<ScanResult> results = this.wifi.getScanResults();
			ScanList.add(results);
//			ScanResult bestSignal = null;
//			for (ScanResult result : results) {
//				if (bestSignal == null
//						|| WifiManager.compareSignalLevel(bestSignal.level,
//								result.level) < 0)
//					bestSignal = result;
//			}
        }
        for (int i = 1; i < 5; i++) {
        	if ScanList.get(1).
        }
        
        
        
        
        
        //we create a XmlSerializer in order to write xml data
        XmlSerializer serializer = Xml.newSerializer();
        try {
                //we set the FileOutputStream as output for the serializer, using UTF-8 encoding
                        serializer.setOutput(fileos, "UTF-8");
                        //Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null) 
                        serializer.startDocument(null, Boolean.valueOf(true)); 
                        //set indentation option
                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true); 
                        //start a tag called "root"
                        serializer.startTag(null, "root"); 
                        // indent code just to have a view similar to xml-tree
                                serializer.startTag(null, "AP1");
                                serializer.attribute(null, "SSID", "value");
                                serializer.attribute(null, "MacAddr", "value");
                                serializer.attribute(null, "Sig", "value");
                                serializer.endTag(null, "AP1");                          
                                
                                serializer.startTag(null, "AP2");
                                serializer.attribute(null, "SSID", "value");
                                serializer.attribute(null, "MacAddr", "value");
                                serializer.attribute(null, "Sig", "value");
                                serializer.endTag(null, "AP2");                     
                                
                                serializer.startTag(null, "AP3");
                                serializer.attribute(null, "SSID", "value");
                                serializer.attribute(null, "MacAddr", "value");
                                serializer.attribute(null, "Sig", "value");
                                serializer.endTag(null, "AP3");  
                                
                                serializer.startTag(null, "AP4");
                                serializer.attribute(null, "SSID", "value");
                                serializer.attribute(null, "MacAddr", "value");
                                serializer.attribute(null, "Sig", "value");
                                serializer.endTag(null, "AP4");
                                 
                                serializer.startTag(null, "AP5");
                                serializer.attribute(null, "SSID", "value");
                                serializer.attribute(null, "MacAddr", "value");
                                serializer.attribute(null, "Sig", "value");
                                serializer.endTag(null, "AP5");
                                
                        serializer.endTag(null, "root");
                        serializer.endDocument();
                        //write xml data into the FileOutputStream
                        serializer.flush();
                        //finally we close the file stream
                        fileos.close();     
                                
//                TextView tv = (TextView)this.findViewById(R.id.result);
//                        tv.setText("file has been created on SD card");
                } catch (Exception e) {
                	Log.e("Exception","error occurred while creating xml file");
                }
        
		
//		textStatus = (TextView) findViewById(R.id.textStatus);
//		textStatus.setText("");
//		textStatus.append(UserID);
		Toast.makeText(this, "Start Scan now!!", Toast.LENGTH_LONG).show();
		if (view.getId() == R.id.buttonScan) {
			Log.d(TAG, "onClick() wifi.startScan()");

		}
		
		ArrayList<List<ScanResult>> ScanList = new ArrayList<List<ScanResult>>(5);
		for (int scancount = 0; scancount < 5; scancount++) {
			wifi.startScan();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
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

			// TODO Compare 5 scan results and Calculate an average value

			Log.d(TAG, "onReceive() message: " + message);
		}
//		List<ScanResult> average;
//		for(int i=0;i<5;i++){
//			List<ScanResult> res= ScanList.get(i);
//			for(int j=0;j<res.size();i++){
//				
//			}
//			
//		}
	}
}


//public class XmlFileCreator extends Activity {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        //create a new file called "new.xml" in the SD card
//        File newxmlfile = new File(Environment.getExternalStorageDirectory()+"/new.xml");
//        try{
//                newxmlfile.createNewFile();
//        }catch(IOException e){
//                Log.e("IOException", "exception in createNewFile() method");
//        }
//        //we have to bind the new file with a FileOutputStream
//        FileOutputStream fileos = null;         
//        try{
//                fileos = new FileOutputStream(newxmlfile);
//        }catch(FileNotFoundException e){
//                Log.e("FileNotFoundException", "can't create FileOutputStream");
//        }
//        //we create a XmlSerializer in order to write xml data
//        XmlSerializer serializer = Xml.newSerializer();
//        try {
//                //we set the FileOutputStream as output for the serializer, using UTF-8 encoding
//                        serializer.setOutput(fileos, "UTF-8");
//                        //Write <?xml declaration with encoding (if encoding not null) and standalone flag (if standalone not null) 
//                        serializer.startDocument(null, Boolean.valueOf(true)); 
//                        //set indentation option
//                        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true); 
//                        //start a tag called "root"
//                        serializer.startTag(null, "root"); 
//                        //i indent code just to have a view similar to xml-tree
//                                serializer.startTag(null, "child1");
//                                serializer.endTag(null, "child1");                          
//                                serializer.startTag(null, "child2");
//                                //set an attribute called "attribute" with a "value" for <child2>
//                                serializer.attribute(null, "attribute", "value");
//                                serializer.endTag(null, "child2");                     
//                                serializer.startTag(null, "child3");
//                                //write some text inside <child3>
//                                serializer.text("some text inside child3");
//                                serializer.endTag(null, "child3");                               
//                        serializer.endTag(null, "root");
//                        serializer.endDocument();
//                        //write xml data into the FileOutputStream
//                        serializer.flush();
//                        //finally we close the file stream
//                        fileos.close();                       
//                TextView tv = (TextView)this.findViewById(R.id.result);
//                        tv.setText("file has been created on SD card");
//                } catch (Exception e) {
//                        Log.e("Exception","error occurred while creating xml file");
//                }
//    }
//}
