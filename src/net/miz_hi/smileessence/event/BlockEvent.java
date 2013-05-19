package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;

public class BlockEvent extends UserEvent
{

	public BlockEvent(UserModel source)
	{
		super(source);
	}

	@Override
	public String getText()
	{
		return source.screenName + "‚ÉƒuƒƒbƒN‚³‚ê‚½";
	}

}
