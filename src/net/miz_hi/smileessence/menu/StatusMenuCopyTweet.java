package net.miz_hi.smileessence.menu;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.StatusUpdate;
import android.app.Activity;
import android.widget.Toast;

public class StatusMenuCopyTweet extends StatusMenuItemBase
{

	public StatusMenuCopyTweet(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return Client.getPermission().canCopyTweet();
	}

	@Override
	public String getText()
	{
		return "ƒpƒN‚é";
	}

	@Override
	public void work()
	{
		StatusUpdate update = new StatusUpdate(model.text);
		update.setInReplyToStatusId(model.inReplyToStatusId);
		final Future<Boolean> resp = MyExecutor.submit(new AsyncTweetTask(update));
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
							if(result)
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

}
