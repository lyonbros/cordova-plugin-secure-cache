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
	private static final String TAG = "MyActivity";
	private static final int ONGOING_NOTIFICATION_ID = 1;
	private String notificationTitle = "App Service";
	private String notificationText = "Running";
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

	private Notification getActivityNotification(String title, String text)
	{
		//Build a Notification required for running service in foreground.
		Intent main = getApplicationContext().getPackageManager().getLaunchIntentForPackage(getApplicationContext().getPackageName());
		main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 1000, main,  PendingIntent.FLAG_UPDATE_CURRENT);

		int icon = R.drawable.star_big_on;
		int normalIcon = getResources().getIdentifier("icon", "drawable", getPackageName());
		int notificationIcon = getResources().getIdentifier("notification", "drawable", getPackageName());         
		if(notificationIcon != 0) icon = notificationIcon;
		else if(normalIcon != 0) icon = normalIcon;

		Notification.Builder builder = new Notification.Builder(this);
		builder.setContentTitle(title);
		builder.setContentText(text);
		builder.setSmallIcon(icon);
		builder.setContentIntent(pendingIntent);        
		Notification notification;
		notification = builder.build();
		notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
		return notification;
	}

	private void foreground()
	{
		Notification notification = getActivityNotification(notificationTitle, notificationText);
		startForeground(ONGOING_NOTIFICATION_ID, notification);
		Log.i(TAG, "SecureCacheService: start foreground");
	}

	private void unforeground()
	{
		stopForeground(true);
		Log.i(TAG, "SecureCacheService: stop foreground");
	}
}

