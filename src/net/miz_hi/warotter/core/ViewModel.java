package net.miz_hi.warotter.core;

import gueei.binding.labs.EventAggregator;

public abstract class ViewModel
{
	public EventAggregator eventAggregator;

	public ViewModel(){	}
	
	public abstract void onActivityCreated();
	
	public abstract void onActivityResumed();

	public final void setEventAggregator(EventAggregator ea)
	{
		this.eventAggregator = ea;		
	}
}
