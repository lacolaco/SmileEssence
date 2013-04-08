package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.TweetSystem;

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
		TweetSystem.insertText(text);
	}

}
