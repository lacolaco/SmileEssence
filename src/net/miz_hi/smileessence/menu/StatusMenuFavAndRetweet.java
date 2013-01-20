package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;

public class StatusMenuFavAndRetweet extends StatusMenuItemBase
{

	public StatusMenuFavAndRetweet(EventHandlerActivity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "ふぁぼってリツイートする";
	}

	@Override
	public void work()
	{
		AsyncFavoriteTask.addTask(new AsyncFavoriteTask(model.statusId, activity.getMainViewModel()));
		AsyncRetweetTask.addTask(new AsyncRetweetTask(model.statusId, activity.getMainViewModel()));
	}

}
