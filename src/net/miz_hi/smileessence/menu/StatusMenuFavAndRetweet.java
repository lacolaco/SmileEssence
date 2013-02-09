package net.miz_hi.smileessence.menu;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.TwitterManager;
import android.app.Activity;
import android.widget.Toast;

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
		Future<Boolean> f1 = adapter.getExecutor().submit(new AsyncFavoriteTask(model.statusId));
		Future<Boolean> f2 = adapter.getExecutor().submit(new AsyncRetweetTask(model.statusId));
		try
		{
			if (f1.get() && f2.get())
			{
				Toast.makeText(activity, TwitterManager.MESSAGE_RETWEET_SUCCESS, Toast.LENGTH_SHORT).show();
			}
			else
			{
				Toast.makeText(activity, TwitterManager.MESSAGE_SOMETHING_ERROR, Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
