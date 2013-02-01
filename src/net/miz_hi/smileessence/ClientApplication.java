package net.miz_hi.smileessence;

import android.app.Application;

public class ClientApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		Client.initialize(this);
	}
}
