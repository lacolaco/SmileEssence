package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class UserCommandSpam extends UserCommand implements IConfirmable
{

	public UserCommandSpam(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "スパム報告";
	}

	@Override
	public void workOnUiThread()
	{
		MyExecutor.execute(new Runnable()
		{
			@Override
			public void run()
			{
				if (TwitterManager.spam(Client.getMainAccount(), userName))
				{
					Notifier.info("スパム報告しました");
				}
			}
		});
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return !Client.getMainAccount().getScreenName().equals(userName);
	}
}