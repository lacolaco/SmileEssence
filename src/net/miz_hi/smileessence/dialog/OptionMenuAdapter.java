package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.menu.MenuItemEditTemplate;
import net.miz_hi.smileessence.menu.MenuItemOpenFavstar;
import net.miz_hi.smileessence.menu.MenuItemOpenFollowers;
import net.miz_hi.smileessence.menu.MenuItemOpenFriends;
import net.miz_hi.smileessence.menu.MenuItemReConnect;
import net.miz_hi.smileessence.menu.MenuItemReport;
import net.miz_hi.smileessence.menu.MenuItemSetting;
import android.app.Activity;
import android.app.Dialog;

public class OptionMenuAdapter extends DialogAdapter
{
	private String title;
	private int textSize = 15;

	public OptionMenuAdapter(Activity activity, String title)
	{
		super(activity);
		this.title = title;
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{

		if (init)
		{
			list.clear();
			list.add(new MenuItemSetting(activity, this));
			list.add(new MenuItemReConnect(activity, this));
			list.add(new MenuItemEditTemplate(activity, this));
			list.add(new MenuItemOpenFavstar(activity, this));
			list.add(new MenuItemOpenFollowers(activity, this));
			list.add(new MenuItemOpenFriends(activity, this));
			list.add(new MenuItemReport(activity, this));
			
			setTitle("ÉÅÉjÉÖÅ[");
		}
		
		return super.createMenuDialog();
	}

}
