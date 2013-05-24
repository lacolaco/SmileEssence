package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.status.StatusModel;

public class UnfavoriteEvent extends StatusEventModel implements IAttackEvent
{

	public UnfavoriteEvent(UserModel source, StatusModel targetStatus)
	{
		super(source, targetStatus);
	}

	@Override
	public String getHeaderText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(source.screenName).append("にあんふぁぼされた");
		return sb.toString();
	}

}
