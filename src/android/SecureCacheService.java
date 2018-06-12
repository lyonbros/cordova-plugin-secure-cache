package com.lyonbros.securecache;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.util.Log;
import android.os.IBinder;
import android.R;

public class SecureCacheService extends Service
{
	public static final String CACHE_PERMISSION = "com.lyonbros.securecache.ACCESS_SECRETS";
	public static final String RECEIVE_CACHE = "com.lyonbros.securecache.RECEIVE_CACHE";
	private static final String TAG = "SecureCache";
	private static final int ONGOING_NOTIFICATION_ID = 1;
	private String cache;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		Log.i(TAG, "created");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid)
	{
		if(intent == null) {
			return START_STICKY;
		}
		String action = intent.getStringExtra("action");
		if(action == null) {
			return START_STICKY;
		}
		Log.i(TAG, "action: "+action);
		if(action.equals("start")) {
			// hey, girl heyy
			Log.i(TAG, "start");
			respond("true");
		} else if(action.equals("set")) {
			String data = intent.getStringExtra("data");
			Log.i(TAG, "set: "+(data || "").length());
			cache = data;
			respond("true");
		} else if(action.equals("wipe")) {
			Log.i(TAG, "wipe");
			cache = null;
			respond("true");
		} else if(action.equals("get")) {
			Log.i(TAG, "get: "+(cache || "").length());
			respond(cache);
		} else if(action.equals("stop")) {
			Log.i(TAG, "stop");
			stopSelf();
			respond("true");
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		Log.i(TAG, "destroyed");
	}

	private void respond(String msg)
	{
		Intent intent = new Intent(RECEIVE_CACHE);
		intent.putExtra("data", cache);
		intent.setPackage(getApplicationContext().getPackageName());
		sendBroadcast(intent, CACHE_PERMISSION);
	}
}

