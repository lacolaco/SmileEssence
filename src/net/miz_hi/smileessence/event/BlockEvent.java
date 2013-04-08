package net.miz_hi.smileessence.event;

import twitter4j.User;

public class BlockEvent extends UserEvent
{

	public BlockEvent(User source)
	{
		super(source);
	}

	@Override
	public String getText()
	{
		return source.getScreenName() + "‚ÉƒuƒƒbƒN‚³‚ê‚½";
	}

}
