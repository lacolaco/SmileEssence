package net.miz_hi.smileessence.command.post;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.system.PostSystem;
import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.view.fragment.PostFragment;

public class CommandParseMorse extends MenuCommand
{

	public CommandParseMorse()
	{
	}

	@Override
	public String getName()
	{
		return "モールスに変換";
	}

	@Override
	public void workOnUiThread()
	{
		String newStr = Morse.jaToMc(PostSystem.getText());
		PostSystem.setText(newStr).openPostPage();
	}
}
