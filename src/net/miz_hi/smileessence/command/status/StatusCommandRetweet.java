package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.async.AsyncRetweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandRetweet extends StatusCommand implements IConfirmable
{

	public StatusCommandRetweet(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "リツイート";
	}

	@Override
	public void workOnUiThread()
	{
		final Future<Boolean> resp = MyExecutor.submit(new AsyncRetweetTask(status.statusId));
		MyExecutor.execute(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					if(resp.get())
					{
						Notifier.info(TwitterManager.MESSAGE_RETWEET_SUCCESS);
					}
					else
					{
						Notifier.alert(TwitterManager.MESSAGE_RETWEET_DEPLICATE);
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
