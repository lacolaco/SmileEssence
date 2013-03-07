package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.view.TweetViewManager;

public class CommandParseMorse extends MenuCommand
{

	public CommandParseMorse()
	{
	}

	@Override
	public String getName()
	{
		return "ƒ‚[ƒ‹ƒX‚É•ÏŠ·";
	}

	@Override
	public void workOnUiThread()
	{
		TweetViewManager manager = TweetViewManager.getInstance();
		String str = manager.getText();
		String newStr = Morse.jaToMc(str);
		manager.setText(newStr);
	}
}
