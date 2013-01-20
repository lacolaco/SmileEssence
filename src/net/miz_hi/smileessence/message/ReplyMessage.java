package net.miz_hi.smileessence.message;

import net.miz_hi.smileessence.core.Message;
import twitter4j.User;

public class ReplyMessage implements Message
{
	public User sendTo;
	public long inReplyToStatusId;
	
	public ReplyMessage(User user, long statusId)
	{
		this.sendTo = user;
		this.inReplyToStatusId = statusId;
	}
}
