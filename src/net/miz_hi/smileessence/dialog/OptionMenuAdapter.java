package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.menu.MenuItemAccountReset;
import net.miz_hi.smileessence.menu.MenuItemOpenFavstar;
import net.miz_hi.smileessence.menu.MenuItemOpenFollowers;
import net.miz_hi.smileessence.menu.MenuItemOpenFriends;
import net.miz_hi.smileessence.menu.MenuItemReport;
import net.miz_hi.smileessence.menu.MenuItemSetting;
import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

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

		TextView viewTitle = new TextView(activity);
		viewTitle.setTextSize(textSize);
		viewTitle.setTextColor(Client.getResource().getColor(R.color.White));
		viewTitle.setText(title);
		viewTitle.setPadding(10, 10, 0, 10);

		if (init)
		{
			list.clear();
			list.add(new MenuItemSetting(activity, this));
			list.add(new MenuItemOpenFavstar(activity, this));
			list.add(new MenuItemOpenFollowers(activity, this));
			list.add(new MenuItemOpenFriends(activity, this));
			list.add(new MenuItemReport(activity, this));
			list.add(new MenuItemAccountReset(activity, this));
		}

		return super.createMenuDialog(viewTitle);
	}

}
