package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.command.user.UserCommandFollow;
import net.miz_hi.smileessence.command.user.UserCommandOpenFavstar;
import net.miz_hi.smileessence.command.user.UserCommandOpenPage;
import net.miz_hi.smileessence.command.user.UserCommandOpenProfiel;
import net.miz_hi.smileessence.command.user.UserCommandRemove;
import net.miz_hi.smileessence.command.user.UserCommandReply;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.app.Dialog;

public class EventCommandDialog extends DialogAdapter
{

	private EventModel model;

	public EventCommandDialog(Activity activity, EventModel model)
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
