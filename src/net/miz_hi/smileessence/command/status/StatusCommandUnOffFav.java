package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import twitter4j.StatusUpdate;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

public class StatusCommandUnOffFav extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandUnOffFav(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "îÒåˆéÆÇ”ÇüÇ⁄";
	}

	@Override
	public void workOnUiThread()
	{
		String str = "@" + status.user.screenName + " Ç¡Åö";
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(status.statusId);
		MyExecutor.submit(new AsyncFavoriteTask(status.statusId));
		final Future<Boolean> f = MyExecutor.submit(new AsyncTweetTask(update));
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				
				try
				{
					if(f.get())
					{
						ToastManager.toast(TwitterManager.MESSAGE_FAVORITE_SUCCESS);
					}
					else
					{
						ToastManager.toast(TwitterManager.MESSAGE_FAVORITE_DEPLICATE);
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
		return Client.getPermission().canWarotaRT();
	}

}
