package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;

public class BlockEvent extends UserEvent
{

	public BlockEvent(UserModel source)
	{
		super(source);
	}

	@Override
	public String getHeaderText()
	{
		return source.screenName + "にブロックされた";
	}

}
