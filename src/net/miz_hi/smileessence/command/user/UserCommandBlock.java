package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.twitter.User;

public class UserCommandBlock extends UserCommand implements IConfirmable
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
				if (User.block(Client.getMainAccount(), userName))
				{
					Notifier.info(userName + "をブロックしました");
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