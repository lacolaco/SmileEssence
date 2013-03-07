package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import net.miz_hi.smileessence.command.UserCommandBlock;
import net.miz_hi.smileessence.command.UserCommandFollow;
import net.miz_hi.smileessence.command.UserCommandOpenFavstar;
import net.miz_hi.smileessence.command.UserCommandOpenPage;
import net.miz_hi.smileessence.command.UserCommandRemove;
import net.miz_hi.smileessence.command.UserCommandReply;
import net.miz_hi.smileessence.command.UserCommandSpam;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusUtils;
import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

public class UserMenuAdapter extends DialogAdapter
{
	private String userName;
	private boolean isMe;
	private int textSize = 15;

	public UserMenuAdapter(Activity activity, UserModel model)
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
