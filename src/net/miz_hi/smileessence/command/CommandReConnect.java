package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.MainSystem;
import net.miz_hi.smileessence.view.activity.MainActivity;

public class CommandReConnect extends MenuCommand
{

	public CommandReConnect()
	{
	}

	@Override
	public String getName()
	{
		return "ÄÚ‘±";
	}

	@Override
	public void workOnUiThread()
	{
		MainSystem.getInstance().connectUserStream(MainActivity.getInstance());
	}

}
