package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import twitter4j.TwitterException;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

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
						ToastManager.toast("çÌèúÇµÇ‹ÇµÇΩ");
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
		return status.isMine;
	}

}
