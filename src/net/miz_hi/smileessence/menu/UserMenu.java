package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.command.user.UserCommandBlock;
import net.miz_hi.smileessence.command.user.UserCommandFollow;
import net.miz_hi.smileessence.command.user.UserCommandOpenFavstar;
import net.miz_hi.smileessence.command.user.UserCommandOpenPage;
import net.miz_hi.smileessence.command.user.UserCommandRemove;
import net.miz_hi.smileessence.command.user.UserCommandSpam;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.app.Dialog;

public class UserMenu extends DialogAdapter
{
	private String userName;
	private boolean isMe;

	public UserMenu(Activity activity, UserModel model)
	{
		super(activity);
		this.userName = model.screenName;
		this.isMe = model.isMe();
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{
		if (init)
		{
			list.clear();
			list.add(new UserCommandOpenPage(activity, userName));
			list.add(new UserCommandOpenFavstar(activity, userName));
			list.add(new UserCommandFollow(userName));
			list.add(new UserCommandRemove(userName));
			list.add(new UserCommandBlock(userName));
			list.add(new UserCommandSpam(userName));

			setTitle("@" + userName);
		}

		return super.createMenuDialog();
	}
}
