package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.command.CommandEditTemplate;
import net.miz_hi.smileessence.command.CommandOpenFavstar;
import net.miz_hi.smileessence.command.CommandOpenFollowers;
import net.miz_hi.smileessence.command.CommandOpenFriends;
import net.miz_hi.smileessence.command.CommandOpenSetting;
import net.miz_hi.smileessence.command.CommandReConnect;
import net.miz_hi.smileessence.command.CommandReport;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.app.Dialog;

public class OptionMenuAdapter extends DialogAdapter
{

	public OptionMenuAdapter(Activity activity)
	{
		super(activity);
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{

		if (init)
		{
			list.clear();
			list.add(new CommandOpenSetting(activity));
			list.add(new CommandReConnect());
			list.add(new CommandEditTemplate(activity));
			list.add(new CommandOpenFavstar(activity));
			list.add(new CommandReport());
			
			setTitle("ÉÅÉjÉÖÅ[");
		}
		
		return super.createMenuDialog();
	}
}
