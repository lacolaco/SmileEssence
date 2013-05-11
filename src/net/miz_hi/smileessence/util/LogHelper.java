package net.miz_hi.smileessence.util;

import android.util.Log;

public class LogHelper
{

	public static void d(Object obj)
	{
		Log.d("SE", obj.toString());
	}

	public static void printI(Object obj)
	{
		Log.i("SE", obj.toString());
	}
}
