package net.miz_hi.smileessence.command.post;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.system.PostSystem;
import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.view.PostFragment;

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
		String newStr = Morse.jaToMc(PostSystem.getText());
		PostSystem.setText(newStr).openPostPage();
	}
}
