package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import twitter4j.StatusUpdate;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandThankToFav extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandThankToFav(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "ふぁぼあり";
	}

	@Override
	public void workOnUiThread()
	{
		String str = "@" + status.user.screenName + " ふぁぼあり(o^-')b" ;
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(status.statusId);
		new AsyncFavoriteTask(status.statusId).addToQueue();
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
						Notifier.info("ふぁぼありしました");
					}
					else
					{
						Notifier.alert(TwitterManager.MESSAGE_FAVORITE_DEPLICATE);
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