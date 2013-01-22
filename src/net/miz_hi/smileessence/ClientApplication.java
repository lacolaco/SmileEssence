package net.miz_hi.smileessence;

import android.app.Application;
import android.os.StrictMode;

public class ClientApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		Client.initialize(this);
	}
}
