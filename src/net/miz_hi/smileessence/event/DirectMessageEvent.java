package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;

public class DirectMessageEvent extends UserEvent
{

	public DirectMessageEvent(UserModel source)
	{
		super(source);
	}

	@Override
	public String getText()
	{
		return source.screenName + "‚©‚çDM‚ðŽó‚¯Žæ‚Á‚½";
	}

}
