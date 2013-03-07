package net.miz_hi.smileessence.command;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;

public class StatusCommandWarotaRT extends StatusCommand implements IHideable
{

	private static boolean isVisible = true;

	public StatusCommandWarotaRT(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "ワロタ式RT";
	}

	@Override
	public void workOnUiThread()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ワロタｗ RT @");
		builder.append(status.screenName);
		builder.append(": ");
		builder.append(status.text);
		StatusUpdate update = new StatusUpdate(builder.toString());
		update.setInReplyToStatusId(status.statusId);

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
						ToastManager.getInstance().toast(TwitterManager.MESSAGE_TWEET_SUCCESS);
					}
					else
					{
						ToastManager.getInstance().toast(TwitterManager.MESSAGE_SOMETHING_ERROR);
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
	public boolean getIsVisible()
	{
		return isVisible && Client.getPermission().canWarotaRT() && !status.user.isProtected;
	}

	@Override
	public void setIsVisible(boolean value)
	{
		isVisible = value;
	}

}
