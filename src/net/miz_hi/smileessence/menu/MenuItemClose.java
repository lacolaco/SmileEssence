package net.miz_hi.smileessence.menu;

import android.app.Activity;
import net.miz_hi.smileessence.dialog.DialogAdapter;

public class MenuItemClose extends MenuItemBase
{

	public MenuItemClose(Activity activity, DialogAdapter adapter)
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
		_adapter.dispose();
	}

}
