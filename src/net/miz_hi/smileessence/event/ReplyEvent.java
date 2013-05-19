package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.status.StatusModel;

public class ReplyEvent extends StatusEventModel
{

	public ReplyEvent(UserModel user, StatusModel status)
	{
		super(user, status);
	}

	@Override
	public String getText()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(source.screenName).append("‚©‚ç‚Ì•ÔM");
		return sb.toString();
	}
}
