package net.miz_hi.smileessence.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.event.EventModel;
import net.miz_hi.smileessence.event.EventViewFactory;
import net.miz_hi.smileessence.menu.MenuItemBase;
import net.miz_hi.smileessence.menu.MenuItemParent;
import net.miz_hi.smileessence.menu.UserMenuFollow;
import net.miz_hi.smileessence.menu.UserMenuOpenFavstar;
import net.miz_hi.smileessence.menu.UserMenuOpenPage;
import net.miz_hi.smileessence.menu.UserMenuOpenProfiel;
import net.miz_hi.smileessence.menu.UserMenuRemove;
import net.miz_hi.smileessence.menu.UserMenuReply;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

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

			list.add(new UserMenuReply(activity, this, user.screenName));
			list.add(new UserMenuOpenProfiel(activity, this, user.screenName));
			list.add(new UserMenuOpenPage(activity, this, user.screenName));
			list.add(new UserMenuOpenFavstar(activity, this, user.screenName));
			list.add(new UserMenuFollow(activity, this, user.screenName));
			list.add(new UserMenuRemove(activity, this, user.screenName));
			
			setTitle("@"+ user.screenName);
		}

		return super.createMenuDialog();
	}
	


}
