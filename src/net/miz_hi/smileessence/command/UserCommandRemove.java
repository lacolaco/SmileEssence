package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;

public class UserCommandRemove extends UserCommand
{

	public UserCommandRemove(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "ƒŠƒ€[ƒ”‚·‚é";
	}

	@Override
	public void workOnUiThread()
	{
		MyExecutor.execute(new Runnable()
		{

			@Override
			public void run()
			{
				if (TwitterManager.remove(Client.getMainAccount(), userName))
				{
					ToastManager.show("ƒŠƒ€[ƒ”‚µ‚Ü‚µ‚½");
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
