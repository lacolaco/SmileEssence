package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.user.UserCommandFollow;
import net.miz_hi.smileessence.command.user.UserCommandOpenFavstar;
import net.miz_hi.smileessence.command.user.UserCommandOpenInfo;
import net.miz_hi.smileessence.command.user.UserCommandOpenPage;
import net.miz_hi.smileessence.command.user.UserCommandRemove;
import net.miz_hi.smileessence.command.user.UserCommandReply;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.event.EventModel;
import android.app.Activity;
import android.app.Dialog;

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
		items.add(new UserCommandOpenInfo(user.screenName, activity));
		items.add(new UserCommandFollow(user.screenName));
		items.add(new UserCommandRemove(user.screenName));
		return items;
	}
}
