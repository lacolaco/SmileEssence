package net.miz_hi.smileessence.util;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils
{

    public static boolean cannotConnect(Activity activity)
    {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return info == null || !info.isConnected();
    }
}
