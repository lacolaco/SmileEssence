package net.lacolaco.smileessence.logging;

import android.util.Log;

public class Logger
{

    private static final String TAG = "SmileEssence";

    public static void debug(String message)
    {
        Log.d(TAG, message);
    }

    public static void info(String message)
    {
        Log.i(TAG, message);
    }

    public static void error(String message)
    {
        Log.e(TAG, message);
    }

    public static void warn(String message)
    {
        Log.w(TAG, message);
    }

    public static void verbose(String message)
    {
        Log.v(TAG, message);
    }
}
