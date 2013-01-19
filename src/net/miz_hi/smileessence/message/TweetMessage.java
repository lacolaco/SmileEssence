package net.miz_hi.smileessence.message;

import net.miz_hi.smileessence.core.Message;

public class TweetMessage implements Message
{
	public String text;
	
	public TweetMessage(String text)
	{
		this.text = text;
	}
}
