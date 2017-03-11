package data.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class internetServiceStatus extends Service {
	BroadcastReceiver mReceiver = null;
	private PendingIntent pendingIntent;
	private AlarmManager manager;

	@Override
	public void onCreate() {
		super.onCreate();
		Intent alarmIntent = new Intent(this, Receiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
		// Toast.makeText(getBaseContext(), "Service on create",
		// Toast.LENGTH_SHORT).show();

		// Register receiver that handles screen on and screen off logic

		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
		mReceiver = new NetworkChangeReceiver();
		registerReceiver(mReceiver, filter);
	}

	@Override
	public void onStart(Intent intent, int startId) {

		boolean screenOn = false;

		try {
			// Get ON/OFF values sent from receiver ( AEScreenOnOffReceiver.java
			// )
			screenOn = intent.getBooleanExtra("screen_state", false);

		} catch (Exception e) {
		}

		// Toast.makeText(getBaseContext(), "Service on start :"+screenOn,
		// Toast.LENGTH_SHORT).show();

		if (!screenOn) {
			if (manager != null) {
				manager.cancel(pendingIntent);
				// Toast.makeText(this, "Alarm Canceled",
				// Toast.LENGTH_SHORT).show();
			}
			// Some time required to start any service
			// Toast.makeText(getBaseContext(), "Internet Disconnect ",
			// Toast.LENGTH_LONG).show();

		} else {
			manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			int interval = 1000 * 10; // 1 minute

			manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
			// your code here
			// Some time required to stop any service to save battery
			// consumption
			// Toast.makeText(getBaseContext(), "Internet connect",
			// Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {

		//Log.i("ScreenOnOff", "Service  distroy");
		if (mReceiver != null)
			unregisterReceiver(mReceiver);

	}
}