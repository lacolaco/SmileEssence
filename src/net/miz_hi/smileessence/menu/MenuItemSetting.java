package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.activity.SettingActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
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
		activity.startActivity(new Intent(activity, SettingActivity.class));
	}

}
