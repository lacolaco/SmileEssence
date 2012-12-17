package net.miz_hi.warotter;

import gueei.binding.Binder;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
