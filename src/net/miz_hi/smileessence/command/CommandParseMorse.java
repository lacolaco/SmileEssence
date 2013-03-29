package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.view.TweetView;

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
		TweetView manager = TweetView.getInstance();
		TweetSystem system = TweetSystem.getInstance();
		String newStr = Morse.jaToMc(system.getText());
		manager.setText(newStr);
	}
}
