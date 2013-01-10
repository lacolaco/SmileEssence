package net.miz_hi.warotter.core;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import gueei.binding.labs.EventAggregator;

public abstract class ViewModel
{
	public EventAggregator eventAggregator;
	public Activity activity;

	public ViewModel(Activity activity)
	{
		this.activity = activity;
	}

	public void onActivityCreated()
	{
	}

	public void onActivityResumed()
	{
	}
	
	public void onDispose()
	{		
	}
	
	public void onActivityResult(int reqCode, int resultCode, Intent intent)
	{
		
	}
	
	public void toast(String string)
	{
		if(activity != null)
		{
			Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
		}
	}

	public final void setEventAggregator(EventAggregator ea)
	{
		this.eventAggregator = ea;
	}
}
