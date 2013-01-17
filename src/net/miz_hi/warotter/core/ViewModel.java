package net.miz_hi.warotter.core;

import android.content.Intent;

public abstract class ViewModel
{
	public Messenger messenger = new Messenger();

	public abstract void onActivityCreated(EventHandlerActivity activity);

	public void onActivityResumed(EventHandlerActivity activity)
	{
	}
	
	public void onActivityPaused(EventHandlerActivity activity)
	{
	}

	public abstract void onActivityDestroy(EventHandlerActivity activity);

	public void onActivityResult(EventHandlerActivity activity, int reqCode, int resultCode, Intent intent)
	{
	}
	
	public abstract boolean onEvent(String eventName, EventHandlerActivity activity);

	public void toast(String string)
	{
		messenger.raise("toast", new ToastMessage(string));
	}

	public final void setMessenger(Messenger messenger)
	{
		this.messenger = messenger;
	}
}
