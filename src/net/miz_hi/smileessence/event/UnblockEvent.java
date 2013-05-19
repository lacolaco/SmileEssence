package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;

public class UnblockEvent extends UserEvent
{

	public UnblockEvent(UserModel source)
	{
		super(source);
	}

	@Override
	public String getHeaderText()
	{
		return source.screenName + "‚ÉƒuƒƒbƒN‰ğœ‚³‚ê‚½";
	}

}
