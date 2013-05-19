package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import twitter4j.TwitterException;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandDelete extends StatusCommand implements IConfirmable
{

	public StatusCommandDelete(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "çÌèú";
	}

	@Override
	public void workOnUiThread()
	{
		final Future<Boolean> f = MyExecutor.submit(new Callable<Boolean>()
		{

			@Override
			public Boolean call()
			{
				try
				{
					TwitterManager.getTwitter(Client.getMainAccount()).destroyStatus(status.statusId);
					return true;
				}
				catch (TwitterException e)
				{
					e.printStackTrace();
				}
				return false;
			}
		});
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					if(f.get())
					{
						Notifier.info("çÌèúÇµÇ‹ÇµÇΩ");
					}
					else
					{
						Notifier.alert(TwitterManager.MESSAGE_SOMETHING_ERROR);
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
		return status.isMine;
	}

}
