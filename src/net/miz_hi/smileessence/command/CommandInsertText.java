package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.TweetViewManager;

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
		TweetViewManager.getInstance().insertText(text);
	}

}
