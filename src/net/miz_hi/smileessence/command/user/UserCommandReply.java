package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.system.PostSystem;

public class UserCommandReply extends UserCommand
{

	public UserCommandReply(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "返信";
	}

	@Override
	public void workOnUiThread()
	{
		PostSystem.setReply(userName, -1);
		PostSystem.openPostPage();
	}

}
