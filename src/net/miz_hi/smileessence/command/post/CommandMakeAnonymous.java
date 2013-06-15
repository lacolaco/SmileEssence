package net.miz_hi.smileessence.command.post;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.system.PostSystem;

public class CommandMakeAnonymous extends MenuCommand
{

	@Override
	public String getName()
	{
		return "匿名にする";
	}

	@Override
	public void workOnUiThread()
	{
		String str = "？？？「" + PostSystem.getText() + "」";
		PostSystem.setText(str).openPostPage();
	}

}
