package net.miz_hi.smileessence.command.main;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.view.SettingActivity;
import android.app.Activity;
import android.content.Intent;

public class CommandOpenSetting extends MenuCommand
{

	private Activity activity;

	public CommandOpenSetting(Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "ê›íË";
	}

	@Override
	public void workOnUiThread()
	{
		activity.startActivity(new Intent(activity, SettingActivity.class));
	}

}
