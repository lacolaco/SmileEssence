package net.miz_hi.warotter;

import net.miz_hi.warotter.model.Warotter;
import android.app.Application;
import android.os.StrictMode;

public class WarotterApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		Warotter.initialize(this);
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
	}
}
