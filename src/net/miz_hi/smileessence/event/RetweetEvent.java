package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.status.StatusModel;

public class RetweetEvent extends StatusEventModel implements IAttackEvent
{

	public RetweetEvent(UserModel retweeter, StatusModel status)
	{
		super(retweeter, status);
	}

	@Override
	public String getHeaderText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(source.screenName).append("にリツイートされた");
		return sb.toString();
	}
}
