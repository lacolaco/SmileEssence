package net.miz_hi.smileessence.system;

import net.miz_hi.smileessence.Client;
import android.app.Activity;

public class CoreSystem
{
	
	private static Activity mainActivity;

	public static void start(Activity activity)
	{
		mainActivity = activity;
		
		if (Client.hasAuthedAccount())
		{
			
		}
	}
	
	public static void work()
	{
		
	}
	
	public static void close()
	{
		
	}
}
