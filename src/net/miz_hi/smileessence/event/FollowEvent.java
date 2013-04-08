package net.miz_hi.smileessence.event;

import twitter4j.User;

public class FollowEvent extends UserEvent
{

	public FollowEvent(User source)
	{
		super(source);
	}

	@Override
	public String getText()
	{
		return source.getScreenName() + "Ç…ÉtÉHÉçÅ[Ç≥ÇÍÇΩ";
	}

}
