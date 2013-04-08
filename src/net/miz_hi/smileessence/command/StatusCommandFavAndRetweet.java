package net.miz_hi.smileessence.command;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

public class StatusCommandFavAndRetweet extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandFavAndRetweet(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "ふぁぼってリツイートする";
	}

	@Override
	public void workOnUiThread()
	{
		final Future<Boolean> f1 = MyExecutor.submit(new AsyncFavoriteTask(status.statusId));
		final Future<Boolean> f2 = MyExecutor.submit(new AsyncRetweetTask(status.statusId));
		MyExecutor.execute(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					if (f1.get() && f2.get())
					{
						ToastManager.toast(TwitterManager.MESSAGE_RETWEET_SUCCESS);
					}
					else
					{
						ToastManager.toast(TwitterManager.MESSAGE_SOMETHING_ERROR);
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return !status.user.isProtected;
	}
}
