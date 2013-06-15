package net.miz_hi.smileessence.command.status;

import java.util.Random;
import java.util.concurrent.Future;

import twitter4j.StatusUpdate;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavorite;
import net.miz_hi.smileessence.async.AsyncTweet;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandCongrats extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandCongrats(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "お祝いする";
	}

	@Override
	public void workOnUiThread()
	{
		int favCount;
		Random rand = new Random();
		int r = rand.nextInt(100);
		if (r < 50)
		{
			favCount = 50;
		}
		else if (r < 80)
		{
			favCount = 100;
		}
		else if (r < 90)
		{
			favCount = 250;
		}
		else if (r < 99)
		{
			favCount = 1000;
		}
		else
		{
			favCount = 10000;
		}

		String str = "@" + status.user.screenName + " Congrats on your " + favCount + "★ tweet! http://favstar.fm/t/" + status.statusId;
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(status.statusId);
		new AsyncFavorite(status.statusId).addToQueue();
		final Future<Boolean> f = MyExecutor.submit(new AsyncTweet(update));
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					if(f.get())
					{
						Notifier.info("お祝いしました");
					}
					else
					{
						Notifier.info(TwitterManager.MESSAGE_SOMETHING_ERROR);
					}
				}
				catch(Exception e)
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
