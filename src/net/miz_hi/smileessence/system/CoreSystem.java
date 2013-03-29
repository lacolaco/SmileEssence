package net.miz_hi.smileessence.system;

import twitter4j.TwitterStream;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.dialog.ProgressDialogHelper;
import net.miz_hi.smileessence.listener.MyUserStreamListener;
import android.app.Activity;
import android.app.ProgressDialog;

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
