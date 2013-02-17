package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;

public class MenuItemReConnect extends MenuItemBase
{

	public MenuItemReConnect(Activity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "çƒê⁄ë±";
	}

	@Override
	public void work()
	{
		MainActivity.getInstance().connectUserStream();
	}

}
