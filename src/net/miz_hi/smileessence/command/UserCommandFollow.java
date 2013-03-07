package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

public class UserCommandFollow extends UserCommand
{

	public UserCommandFollow(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "フォローする";
	}

	@Override
	public void workOnUiThread()
	{
		MyExecutor.execute(new Runnable()
		{

			@Override
			public void run()
			{
				if (TwitterManager.follow(Client.getMainAccount(), userName))
				{
					ToastManager.getInstance().toast("フォローしました");
				}
			}
		});
	}

	@Override
	public boolean getIsVisible()
	{
		return !Client.getMainAccount().getScreenName().equals(userName);
	}

}
