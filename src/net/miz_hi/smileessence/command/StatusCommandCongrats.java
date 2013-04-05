package net.miz_hi.smileessence.command;

import java.util.Random;
import java.util.concurrent.Future;

import twitter4j.StatusUpdate;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

public class StatusCommandCongrats extends StatusCommand implements IHideable
{

	public StatusCommandCongrats(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "Ç®èjÇ¢Ç∑ÇÈ";
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

		String str = "@" + status.user.screenName + " Congrats on your " + favCount + "Åö tweet! http://favstar.fm/t/" + status.statusId;
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
						ToastManager.show("Ç®èjÇ¢ÇµÇ‹ÇµÇΩ");
					}
					else
					{
						ToastManager.show(TwitterManager.MESSAGE_SOMETHING_ERROR);
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
