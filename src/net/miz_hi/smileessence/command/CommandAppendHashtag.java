package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.TweetViewManager;

public class CommandAppendHashtag extends MenuCommand
{

	private String hashtag;

	public CommandAppendHashtag(String hashtag)
	{
		this.hashtag = hashtag;
	}

	@Override
	public String getName()
	{
		return "#" + hashtag;
	}

	@Override
	public void workOnUiThread()
	{
		TweetViewManager.getInstance().appendText(" #" + hashtag);
		TweetViewManager.getInstance().open();
	}
}
