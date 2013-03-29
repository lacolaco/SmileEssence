package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.view.TweetView;

public class CommandInsertText extends MenuCommand
{

	private String text;

	public CommandInsertText(String text)
	{
		this.text = text;
	}

	@Override
	public String getName()
	{
		return text;
	}

	@Override
	public void workOnUiThread()
	{
		TweetSystem.getInstance().insertText(text);
	}

}
