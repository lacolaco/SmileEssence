package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.status.StatusModel;

public class FavoriteEvent extends StatusEventModel implements IAttackEvent
{
	
	public FavoriteEvent(UserModel source, StatusModel targetStatus)
	{
		super(source, targetStatus);

	}

	@Override
	public String getText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(source.screenName).append("‚É‚Ó‚Ÿ‚Ú‚ç‚ê‚½");
		return sb.toString();
	}
}
