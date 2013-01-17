package net.miz_hi.warotter.viewmodel;

import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.core.ViewModel;

public class MenuItemViewModel extends ViewModel
{
	public StringObservable description = new StringObservable();

	public Runnable action;

	public final void run()
	{
		if (action != null)
		{
			action.run();
		}
	}

	public MenuItemViewModel(String description)
	{
		this.description.set(description);
	}

	public MenuItemViewModel setCommand(Runnable action)
	{
		this.action = action;
		return this;
	}

	@Override
	public void onActivityCreated(EventHandlerActivity activity)
	{		
	}

	@Override
	public void onActivityDestroy(EventHandlerActivity activity)
	{	
	}

	@Override
	public boolean onEvent(String eventName, EventHandlerActivity activity)
	{
		return false;	
	}
}
