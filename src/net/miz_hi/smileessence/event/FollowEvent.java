package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;

public class FollowEvent extends UserEvent
{

	public FollowEvent(UserModel source)
	{
		super(source);
	}

	@Override
	public String getHeaderText()
	{
		return source.screenName + "にフォローされた";
	}

}
