package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.user.UserCommandFollow;
import net.miz_hi.smileessence.command.user.UserCommandOpenInfo;
import net.miz_hi.smileessence.command.user.UserCommandUnfollow;
import net.miz_hi.smileessence.command.user.UserCommandReply;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.model.status.event.EventModel;
import net.miz_hi.smileessence.model.status.user.UserModel;
import android.app.Activity;

public class EventMenu extends SimpleMenuDialog
{

	private EventModel model;
	private UserModel user;

	public EventMenu(Activity activity, EventModel model)
	{
		super(activity);
		this.model = model;
		this.user = model.source;
		setTitle("@"+ this.user.screenName);
	}

	@Override
	public List<ICommand> getMenuList()
	{
		List<ICommand> items = new ArrayList<ICommand>();
		items.add(new UserCommandReply(user.screenName));
		items.add(new UserCommandOpenInfo(activity, user.screenName));
		items.add(new UserCommandFollow(user.screenName));
		items.add(new UserCommandUnfollow(user.screenName));
		return items;
	}
}
