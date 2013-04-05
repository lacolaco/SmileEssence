package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.view.TweetView;

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
		TweetSystem.getInstance().appendText(" #" + hashtag);
		TweetView.open();
	}
}
