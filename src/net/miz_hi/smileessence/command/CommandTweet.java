package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.TweetView;

public class CommandTweet extends MenuCommand
{

	@Override
	public String getName()
	{
		return "�Ԃ₭";
	}

	@Override
	public void workOnUiThread()
	{
		TweetView.open();
	}

}
