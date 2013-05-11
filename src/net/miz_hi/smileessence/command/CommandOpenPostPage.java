package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.system.PostSystem;
import net.miz_hi.smileessence.view.MainActivity;

public class CommandOpenPostPage extends MenuCommand
{

	@Override
	public String getName()
	{
		return "ìäçeâÊñ Ç÷";
	}

	@Override
	public void workOnUiThread()
	{
		PostSystem.openPostPage();
	}

}
