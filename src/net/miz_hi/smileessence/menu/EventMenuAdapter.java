package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.command.UserCommandFollow;
import net.miz_hi.smileessence.command.UserCommandOpenFavstar;
import net.miz_hi.smileessence.command.UserCommandOpenPage;
import net.miz_hi.smileessence.command.UserCommandOpenProfiel;
import net.miz_hi.smileessence.command.UserCommandRemove;
import net.miz_hi.smileessence.command.UserCommandReply;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.event.EventModel;
import android.app.Activity;
import android.app.Dialog;

public class EventMenuAdapter extends DialogAdapter
{

	private EventModel model;

	public EventMenuAdapter(Activity activity, EventModel model)
	{
		super(activity);
		this.model = model;
	}

	@Override
	public Dialog createMenuDialog(boolean init)
	{		
		UserModel user = UserStore.put(model.source);
		if(init)
		{
			list.clear();

			list.add(new UserCommandReply(user.screenName));
			list.add(new UserCommandOpenProfiel(activity, user.screenName));
			list.add(new UserCommandOpenPage(activity, user.screenName));
			list.add(new UserCommandOpenFavstar(activity, user.screenName));
			list.add(new UserCommandFollow(user.screenName));
			list.add(new UserCommandRemove(user.screenName));
			
			setTitle("@"+ user.screenName);
		}

		return super.createMenuDialog();
	}
	


}
