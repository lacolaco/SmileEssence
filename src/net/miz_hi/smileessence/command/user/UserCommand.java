package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.command.MenuCommand;


public abstract class UserCommand extends MenuCommand
{

	protected String userName;

	public UserCommand(String userName)
	{
		this.userName = userName;
	}

}
