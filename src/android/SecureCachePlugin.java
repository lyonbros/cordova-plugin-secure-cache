package com.lyonbros.securecache;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SecureCachePlugin extends CordovaPlugin
{
	private CallbackContext cb = null;

	@Override
	public void initialize(CordovaInterface cordova, CordovaWebView webView)
	{
		super.initialize(cordova, webView);

		LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
		IntentFilter filter = new IntentFilter();
		intentFilter.addAction(SecureCacheService.RECEIVE_CACHE);
		manager.registerReceiver(bReceiver, intentFilter);
	}

	private void comm(Intent intent, CallbackContext callback)
	{
		cb = callback;
		startService(intent);
	}

	private BroadcastReceiver bReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if(intent.getAction().equals(SecureCacheService.RECEIVE_CACHE))
			{
				String data = intent.getStringExtra("data");
				result(data);
			}
		}
	}

	private void result(String data)
	{
		if(!cb) return;
		cb.success(data);
		cb = null;
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callback) throws JSONException
	{
		if(cb)
		{
			callback.error("in_use");
			return false;
		}

		Intent intent = new Intent(this, SecureCacheService.class);
		intent.putExtra("action", action);
		if(action.equals("set"))
		{
			intent.putExtra("data", args.getString(0));
			comm(intent, callback);
			return true;
		}
		else if(action.equals("get") || action.equals("foreground") || action.equals("unforeground"))
		{
			comm(intent, callback);
			return true;
		}
		else
		{
			cb = null;
		}
		return false;
	}
}

