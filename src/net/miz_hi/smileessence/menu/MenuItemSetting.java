package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.SettingActivity;
import android.app.Activity;
import android.content.Intent;

public class MenuItemSetting extends MenuItemBase
{

	public MenuItemSetting(Activity activity, DialogAdapter adapter)
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
		_activity.startActivity(new Intent(_activity, SettingActivity.class));
	}

}
