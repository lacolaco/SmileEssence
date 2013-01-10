package net.miz_hi.warotter.viewmodel;

import android.app.Activity;
import android.view.View;
import gueei.binding.Command;
import gueei.binding.observables.StringObservable;
import net.miz_hi.warotter.core.ViewModel;

public class MenuItemViewModel extends ViewModel implements Runnable
{
	public StringObservable description = new StringObservable();
	
	public Runnable action;

	@Override
	public void run()
	{
		if(action != null)
		{
			action.run();
		}
	}

	public MenuItemViewModel(Activity activity, String description)
	{
		super(activity);
		this.description.set(description);
	}
	
	public MenuItemViewModel setCommand(Runnable action)
	{
		this.action = action;
		return this;
	}
}
