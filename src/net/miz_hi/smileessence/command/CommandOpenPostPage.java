package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.PostSystem;

public class CommandOpenPostPage extends MenuCommand
{

	@Override
	public String getName()
	{
		return "つぶやく";
	}

	@Override
	public void workOnUiThread()
	{
		PostSystem.openPostPage();
	}

}
