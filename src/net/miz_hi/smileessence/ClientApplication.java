package net.miz_hi.smileessence;

import net.miz_hi.smileessence.model.Client;
import android.app.Application;
import android.os.StrictMode;

public class ClientApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		Client.initialize(this);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
	}
}
