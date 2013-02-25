package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.menu.UserMenuBlock;
import net.miz_hi.smileessence.menu.UserMenuFollow;
import net.miz_hi.smileessence.menu.UserMenuOpenFavstar;
import net.miz_hi.smileessence.menu.UserMenuOpenPage;
import net.miz_hi.smileessence.menu.UserMenuRemove;
import net.miz_hi.smileessence.menu.UserMenuReply;
import net.miz_hi.smileessence.menu.UserMenuSpam;
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
		if (init)
		{
			list.clear();
			list.add(new UserMenuReply(activity, this, userName));
			list.add(new UserMenuOpenPage(activity, this, userName));
			list.add(new UserMenuOpenFavstar(activity, this, userName));
			list.add(new UserMenuFollow(activity, this, userName));
			list.add(new UserMenuRemove(activity, this, userName));
			list.add(new UserMenuBlock(activity, this, userName));
			list.add(new UserMenuSpam(activity, this, userName));
			
			setTitle("@" + userName);
		}

		return super.createMenuDialog();
	}

}
