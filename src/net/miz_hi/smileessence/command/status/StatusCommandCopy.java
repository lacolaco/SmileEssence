package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;

public class StatusCommandCopy extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandCopy(StatusModel model)
	{
		super(model);
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return Client.getPermission().canCopyTweet();
	}

	@Override
	public String getName()
	{
		return "ƒpƒN‚é";
	}

	@Override
	public void workOnUiThread()
	{
		StatusUpdate update = new StatusUpdate(status.text);
		update.setInReplyToStatusId(status.inReplyToStatusId);
		final Future<Boolean> resp = MyExecutor.submit(new AsyncTweetTask(update));
		MyExecutor.submit(new AsyncFavoriteTask(status.statusId));
		MyExecutor.execute(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					boolean result = resp.get();
					if (result)
					{
						ToastManager.toast(TwitterManager.MESSAGE_TWEET_SUCCESS);
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

}
