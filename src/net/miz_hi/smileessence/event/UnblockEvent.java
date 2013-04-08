package net.miz_hi.smileessence.event;

import twitter4j.User;

public class UnblockEvent extends UserEvent
{

	public UnblockEvent(User source)
	{
		super(source);
	}

	@Override
	public String getText()
	{
		return source.getScreenName() + "‚ÉƒuƒƒbƒN‰ğœ‚³‚ê‚½";
	}

}
