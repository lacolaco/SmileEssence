package net.miz_hi.warotter.menuitem;

import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.dialog.DialogAdapter;
import net.miz_hi.warotter.message.ToastMessage;

public abstract class MenuItemBase
{
	protected EventHandlerActivity activity;
	protected DialogAdapter adapter;

	public MenuItemBase(EventHandlerActivity activity, DialogAdapter factory)
	{
		this.activity = activity;
		this.adapter = factory;
	}

	public abstract String getText();

	public void run()
	{
		new Thread(new Runnable()
		{
			public void run()
			{
				adapter.dispose();
				work();
			}
		}).start();
	}

	public abstract void work();

	public void toast(String str)
	{
		activity.messenger.raise("toast", new ToastMessage(str));
	}

}
