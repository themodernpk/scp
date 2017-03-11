package data.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
	private boolean isConnected = false;

	@Override
	public void onReceive(final Context context, final Intent intent) {
		boolean screenOff;
		// Log.v(LOG_TAG, "Receieved notification about network status");
		screenOff = isNetworkAvailable(context);
		Intent i = new Intent(context, internetServiceStatus.class);
		i.putExtra("screen_state", screenOff);
		context.startService(i);
	}

	private boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						if (!isConnected) {
							// Log.v(LOG_TAG, "Now you are connected to
							// Internet!");
							// networkStatus.setText("Now you are connected to
							// Internet!");
							// Toast.makeText(getApplication(), "Now you are
							// connected to Internet!",
							// Toast.LENGTH_LONG).show();
							isConnected = true;
							// do your processing here ---
							// if you need to post any data to the server or get
							// status
							// update from the server
						}
						return true;
					}
				}
			}
		}

		isConnected = false;
		return false;
	}
}