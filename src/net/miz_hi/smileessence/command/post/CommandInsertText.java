package net.miz_hi.smileessence.command.post;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.system.PostSystem;
import net.miz_hi.smileessence.view.fragment.impl.PostFragment;

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
		PostSystem.insertText(text);
		PostSystem.openPostPage();
	}

}
