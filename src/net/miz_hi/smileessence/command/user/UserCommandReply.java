package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.view.TweetView;

public class UserCommandReply extends UserCommand
{

	public UserCommandReply(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "ƒŠƒvƒ‰ƒC‚ð‘—‚é";
	}

	@Override
	public void workOnUiThread()
	{
		TweetSystem.setReply(userName, -1);
		TweetView.open();
	}

}
