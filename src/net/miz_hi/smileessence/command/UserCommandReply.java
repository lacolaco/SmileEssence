package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.TweetViewManager;

public class UserCommandReply extends UserCommand
{

	public UserCommandReply(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "リプライを送る";
	}

	@Override
	public void workOnUiThread()
	{
		TweetViewManager manager = TweetViewManager.getInstance();
		manager.setReply(userName, -1);
		manager.open();
	}

	@Override
	public boolean getIsVisible()
	{
		return true;
	}

}
