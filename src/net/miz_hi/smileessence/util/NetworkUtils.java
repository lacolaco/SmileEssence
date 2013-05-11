package net.miz_hi.smileessence.util;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils
{
	public static boolean canConnect(Activity activity)
	{
		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if(info != null && info.isConnected())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
