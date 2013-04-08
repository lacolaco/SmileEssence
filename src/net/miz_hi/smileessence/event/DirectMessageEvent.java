package net.miz_hi.smileessence.event;

import twitter4j.User;

public class DirectMessageEvent extends UserEvent
{

	public DirectMessageEvent(User source)
	{
		super(source);
	}

	@Override
	public String getText()
	{
		return source.getScreenName() + "‚©‚çDM‚ğó‚¯æ‚Á‚½";
	}

}
