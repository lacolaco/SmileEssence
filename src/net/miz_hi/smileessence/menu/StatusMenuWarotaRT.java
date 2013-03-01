package net.miz_hi.smileessence.menu;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;
import android.app.Activity;
import android.widget.Toast;

public class StatusMenuWarotaRT extends StatusMenuItemBase
{

	public StatusMenuWarotaRT(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public String getText()
	{
		return "ÉèÉçÉ^éÆRT";
	}

	@Override
	public void work()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ÉèÉçÉ^Çó RT @");
		builder.append(model.screenName);
		builder.append(": ");
		builder.append(model.text);
		StatusUpdate update = new StatusUpdate(builder.toString());
		update.setInReplyToStatusId(model.statusId);

		final Future<Boolean> resp = MyExecutor.submit(new AsyncTweetTask(update));
		MyExecutor.submit(new AsyncFavoriteTask(model.statusId));
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					final boolean result = resp.get();
					new UiHandler()
					{
						
						@Override
						public void run()
						{
							if (result)
							{
								Toast.makeText(activity, TwitterManager.MESSAGE_TWEET_SUCCESS, Toast.LENGTH_SHORT).show();
							}
							else
							{
								Toast.makeText(activity, TwitterManager.MESSAGE_SOMETHING_ERROR, Toast.LENGTH_SHORT).show();
							}
						}
					}.post();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				
			}
		});
	}

	@Override
	public boolean isVisible()
	{
		return Client.getPermission().canWarotaRT() && !model.user.isProtected;
	}

}
