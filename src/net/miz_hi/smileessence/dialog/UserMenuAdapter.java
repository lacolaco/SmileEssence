package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.menu.UserMenuFollow;
import net.miz_hi.smileessence.menu.UserMenuOpenFavstar;
import net.miz_hi.smileessence.menu.UserMenuOpenPage;
import net.miz_hi.smileessence.menu.UserMenuRemove;
import net.miz_hi.smileessence.menu.UserMenuReply;
import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

public class UserMenuAdapter extends DialogAdapter
{
	private String userName;
	private int textSize = 15;

	public UserMenuAdapter(Activity activity, String userName)
	{
		super(activity);
		this.userName = userName;
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{

		TextView viewTitle = new TextView(activity);
		viewTitle.setTextSize(textSize);
		viewTitle.setTextColor(Client.getResource().getColor(R.color.White));
		viewTitle.setText(userName);
		viewTitle.setPadding(10, 10, 0, 10);

		if (init)
		{
			list.clear();
			list.add(new UserMenuReply(activity, this, userName));
			list.add(new UserMenuOpenPage(activity, this, userName));
			list.add(new UserMenuOpenFavstar(activity, this, userName));
			list.add(new UserMenuFollow(activity, this, userName));
			list.add(new UserMenuRemove(activity, this, userName));
		}

		return super.createMenuDialog(viewTitle);
	}

}
