package net.miz_hi.warotter;

import net.miz_hi.warotter.model.Warotter;
import gueei.binding.Binder;
import android.app.Application;

public class WarotterApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		Binder.init(this);
		Warotter.initialize(this);
	}
}
