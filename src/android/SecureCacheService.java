package com.lyonbros.securecache;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.util.Log;

import com.lyonbros.securecache.R;

public class SecureCacheService extends Service
{
	public static final String CACHE_PERMISSION = "com.lyonbros.securecache.ACCESS_SECRETS";
	public static final String RECEIVE_CACHE = "com.lyonbros.securecache.RECEIVE_CACHE";
	private static final String TAG = "MyActivity";
	private static final int ONGOING_NOTIFICATION_ID = 1;
	private String cache;

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "SecureCacheService: created");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid)
	{
		String action = intent.getStringExtra("action");
		Log.i(TAG, "SecureCacheService: action: "+action);
		if(action.equals("set"))
		{
			String data = intent.getStringExtra("data");
			cache = data;
			respond("true");
		}
		else if(action.equals("get"))
		{
			respond(cache);
		}
		else if(action.equals("foreground"))
		{
			foreground();
			respond("true");
		}
		else if(action.equals("unforeground"))
		{
			unforeground();
			respond("true");
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.i(TAG, "SecureCacheService: destroyed");
	}

	private void respond(String msg)
	{
		Intent intent = new Intent(RECEIVE_CACHE);
		intent.putExtra("data", cache);
		intent.setPackage(getApplicationContext().getPackageName());
		sendBroadcast(intent, CACHE_PERMISSION);
	}

	private void foreground()
	{
		Notification notification = new Notification(R.drawable.icon, getText(R.string.ticker_text), System.currentTimeMillis());
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(this, getText(R.string.notification_title), getText(R.string.notification_message), pendingIntent);
		startForeground(ONGOING_NOTIFICATION_ID, notification);
		Log.i(TAG, "SecureCacheService: start foreground");
	}

	private void unforeground()
	{
		stopForeground(true);
		Log.i(TAG, "SecureCacheService: stop foreground");
	}
}

