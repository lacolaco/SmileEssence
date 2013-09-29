package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.user.UserCommandBlock;
import net.miz_hi.smileessence.command.user.UserCommandFollow;
import net.miz_hi.smileessence.command.user.UserCommandOpenFavstar;
import net.miz_hi.smileessence.command.user.UserCommandOpenPage;
import net.miz_hi.smileessence.command.user.UserCommandOpenTimeline;
import net.miz_hi.smileessence.command.user.UserCommandUnfollow;
import net.miz_hi.smileessence.command.user.UserCommandSpam;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.model.status.user.UserModel;
import android.app.Activity;

public class UserMenu extends SimpleMenuDialog
{
	private String userName;
	private boolean isMe;

	public UserMenu(Activity activity, UserModel model)
	{
		super(activity);
		this.userName = model.screenName;
		this.isMe = model.isMe();
		setTitle("@" + userName);
	}

	@Override
	public List<ICommand> getMenuList()
	{
		List<ICommand> items = new ArrayList<ICommand>();
		
		items.add(new UserCommandOpenTimeline(activity, userName));
		items.add(new UserCommandOpenPage(activity, userName));
		items.add(new UserCommandOpenFavstar(activity, userName));
		items.add(new UserCommandFollow(userName));
		items.add(new UserCommandUnfollow(userName));
		items.add(new UserCommandBlock(userName));
		items.add(new UserCommandSpam(userName));		
		return items;
	}
}
