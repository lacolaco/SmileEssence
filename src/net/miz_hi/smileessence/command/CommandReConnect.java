package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.view.activity.MainActivity;

public class CommandReConnect extends MenuCommand
{

	public CommandReConnect()
	{
	}

	@Override
	public String getName()
	{
		return "再接続";
	}

	@Override
	public void workOnUiThread()
	{
		TwitterManager.openTwitterStream(MainActivity.getInstance());
	}

}
