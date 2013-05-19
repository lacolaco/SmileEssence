package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;

public abstract class UserEvent extends EventModel
{

	public UserEvent(UserModel source)
	{
		super(source);
	}
	
}
