package net.miz_hi.smileessence.command.status;

import twitter4j.TwitterException;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandUnfavorite extends StatusCommand implements IHideable
{

	public StatusCommandUnfavorite(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "‚¨‹C‚É“ü‚è‚ğíœ‚·‚é";
	}
	
	@Override
	public void workOnUiThread()
	{
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				try
				{
					TwitterManager.getTwitter().destroyFavorite(status.statusId);
					Notifier.info("‚¨‹C‚É“ü‚è‚ğíœ‚µ‚Ü‚µ‚½");
				}
				catch (TwitterException e)
				{
					e.printStackTrace();
					Notifier.alert("‚¨‹C‚É“ü‚è‚Ìíœ‚É¸”s‚µ‚Ü‚µ‚½");
				}
			}
		});

	}

}
