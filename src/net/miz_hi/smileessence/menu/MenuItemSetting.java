package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.SettingActivity;
import android.content.Intent;

public class MenuItemSetting extends MenuItemBase
{

	public MenuItemSetting(EventHandlerActivity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "ê›íË";
	}

	@Override
	public void work()
	{
		activity.startActivity(new Intent(activity, SettingActivity.class));
	}

}
