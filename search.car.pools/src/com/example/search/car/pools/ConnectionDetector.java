package com.example.search.car.pools;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

public class ConnectionDetector {

	private static Context _context;
	boolean netWorking = false;

	public ConnectionDetector(Context context) {
		this._context = context;
	}

	// public boolean isConnectingToInternet() {
	// ConnectivityManager connectivity = (ConnectivityManager)
	// _context.getSystemService(Context.CONNECTIVITY_SERVICE);
	// if (connectivity != null) {
	// NetworkInfo[] info = connectivity.getAllNetworkInfo();
	// if (info != null)
	// for (int i = 0; i < info.length; i++)
	// if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	// return true;
	// }
	// }
	// return false;
	// return true;
	// }

	public boolean isNetworkAvailable(Context _context) {
		NetworkInfo localNetworkInfo = ((ConnectivityManager) _context.getSystemService("connectivity"))
				.getActiveNetworkInfo();
		if ((localNetworkInfo != null) && (localNetworkInfo.isConnectedOrConnecting())) {
			return true;
		}
		// new AlertDialog.Builder(paramContext).setTitle("Network Error")
		// .setMessage("Internet is not available")
		// .setPositiveButton("OK", null).show();
		return false;
	}

	// --------------------------------- MAIN METHOD FOR INTERNET
	// CONNECTION-----------------------------

	public boolean isConnectingToInternet() {

		if (isNetworkAvailable(_context)) {
			CheckInternet check = new CheckInternet();
			check.execute();
			//Log.i("log", "Network available!");
			return true;
		} else {
			//Log.d("log", "No network available!");
		}
		return false;

		// StrictMode.ThreadPolicy policy = new
		// StrictMode.ThreadPolicy.Builder().permitAll().build();
		// StrictMode.setThreadPolicy(policy);

		// try {
		//
		// Process ipProcess =
		// java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
		// int exitValue = ipProcess.waitFor();
		// return (exitValue == 0);
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// return false;
		// try {
		// InetAddress.getByName("google.com").isReachable(2);
		//
		// return true;
		// } catch (UnknownHostException e) {
		// return false;
		// } catch (IOException e) {
		// return false;
		// }
		// Log.i("log", "Network Status before execute: " + netWorking);
		// CheckInternet check = new CheckInternet();
		// check.execute();
		// Log.i("log", "Network Status after execute: " + netWorking);
		// return netWorking;
	}

	public class CheckInternet extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			//Log.i("log", "Network Status during doInBackGround: " + netWorking);
			String bool = "false";
			try {
				Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 www.google.com");
				int returnVal = p1.waitFor();
				if (returnVal == 0) {
					bool = "true";
					netWorking = true;
				} else
					netWorking = false;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			//Log.i("log", "Network Status after doInBackGround: " + netWorking);
			return bool;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			//Log.i("log", "Network Status onpostexecute: " + netWorking);
			if (result.contentEquals("true"))
				netWorking = true;
			else
				netWorking = false;
			//Log.i("log", "Network Status after onpostexecute: " + netWorking);
		}
	}

}
