package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;

public class MenuItemClose extends MenuItemBase
{

	public MenuItemClose(EventHandlerActivity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "•Â‚¶‚é";
	}

	@Override
	public void work()
	{
		adapter.dispose();
	}

}
