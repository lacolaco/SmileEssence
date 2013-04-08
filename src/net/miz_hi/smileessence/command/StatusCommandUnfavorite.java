package net.miz_hi.smileessence.command;

import twitter4j.TwitterException;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

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
					ToastManager.toast("‚¨‹C‚É“ü‚è‚ğíœ‚µ‚Ü‚µ‚½");
				}
				catch (TwitterException e)
				{
					e.printStackTrace();
					ToastManager.toast("‚¨‹C‚É“ü‚è‚Ìíœ‚É¸”s‚µ‚Ü‚µ‚½");
				}
			}
		});

	}

}
