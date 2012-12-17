package net.miz_hi.warotter.core;

import gueei.binding.labs.EventAggregator;

public abstract class ViewModel
{
	public EventAggregator eventAggregator;

	public ViewModel(){	}
	
	public void onActivityCreated(){};
	
	public void onActivityResumed(){};

	public void setEventAggregator(EventAggregator ea)
	{
		this.eventAggregator = ea;		
	}
}
