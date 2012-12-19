package net.miz_hi.warotter.core;

import android.os.Handler;
import gueei.binding.labs.EventAggregator;

public abstract class ViewModel
{
	public EventAggregator eventAggregator;

	public ViewModel(){	}
	
	public Handler handler = new Handler();
	
	public void onActivityCreated(){};
	
	public void onActivityResumed(){};

	public final void setEventAggregator(EventAggregator ea)
	{
		this.eventAggregator = ea;		
	}
}
