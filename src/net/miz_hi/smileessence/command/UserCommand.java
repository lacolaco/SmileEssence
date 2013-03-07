package net.miz_hi.smileessence.command;


public abstract class UserCommand extends MenuCommand implements IHideable
{

	protected String userName;

	public UserCommand(String userName)
	{
		this.userName = userName;
	}

	@Override
	public abstract boolean getIsVisible();

	@Override
	public void setIsVisible(boolean value)
	{
	};
}
