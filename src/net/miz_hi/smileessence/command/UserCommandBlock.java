package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

public class UserCommandBlock extends UserCommand
{

	public UserCommandBlock(String username)
	{
		super(username);
	}

	@Override
	public String getName()
	{
		return "ブロック";
	}

	@Override
	public void workOnUiThread()
	{
		MyExecutor.execute(new Runnable()
		{
			@Override
			public void run()
			{
				if (TwitterManager.block(Client.getMainAccount(), userName))
				{
					ToastManager.getInstance().toast(userName + "をブロックしました");
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
