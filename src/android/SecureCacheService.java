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
		Log.i(TAG, "SecureCacheService: created");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid)
	{
		if(intent.isNull()) {
			return START_STICKY;
		}
		String action = intent.getStringExtra("action");
		Log.i(TAG, "SecureCacheService: action: "+action);
		if(action.equals("set"))
		{
			String data = intent.getStringExtra("data");
			cache = data;
			respond("true");
		}
		else if(action.equals("wipe"))
		{
			cache = null;
			respond("true");
		}
		else if(action.equals("get"))
		{
			respond(cache);
		}
		else if(action.equals("foreground"))
		{
			String title = intent.getStringExtra("title");
			String text = intent.getStringExtra("text");
			foreground(title, text);
			respond("true");
		}
		else if(action.equals("unforeground"))
		{
			unforeground();
			respond("true");
		}
		else if(action.equals("stop"))
		{
			stopSelf();
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
}

