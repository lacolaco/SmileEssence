package net.miz_hi.smileessence.command.post;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.system.PostSystem;

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
		PostSystem.appendText(" #" + hashtag).openPostPage();
	}
}
