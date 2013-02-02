package net.miz_hi.smileessence.menu;

import android.app.Activity;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.async.ConcurrentAsyncTaskHelper;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;

public class StatusMenuFavAndRetweet extends StatusMenuItemBase
{

	public StatusMenuFavAndRetweet(Activity activity, DialogAdapter adapter, StatusModel model)
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
		ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncFavoriteTask(_model.statusId));
		ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncRetweetTask(_model.statusId));
	}

}
