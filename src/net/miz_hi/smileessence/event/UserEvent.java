package net.miz_hi.smileessence.event;

import twitter4j.User;

public abstract class UserEvent extends EventModel
{

	public UserEvent(User source)
	{
		super(source);
	}
	
}
