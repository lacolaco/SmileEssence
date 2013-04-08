package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.util.Morse;

public class CommandParseMorse extends MenuCommand
{

	public CommandParseMorse()
	{
	}

	@Override
	public String getName()
	{
		return "ÉÇÅ[ÉãÉXÇ…ïœä∑";
	}

	@Override
	public void workOnUiThread()
	{
		String newStr = Morse.jaToMc(TweetSystem.getText());
		TweetSystem.setText(newStr);
	}
}
