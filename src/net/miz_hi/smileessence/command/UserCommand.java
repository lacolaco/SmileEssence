package net.miz_hi.smileessence.command;


public abstract class UserCommand extends MenuCommand
{

	protected String userName;

	public UserCommand(String userName)
	{
		this.userName = userName;
	}

}
