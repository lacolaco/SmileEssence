package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

public class UserCommandSpam extends UserCommand
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
					ToastManager.getInstance().toast("スパム報告しました");
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
