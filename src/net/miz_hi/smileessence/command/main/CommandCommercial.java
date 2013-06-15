package net.miz_hi.smileessence.command.main;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.system.PostSystem;

public class CommandCommercial extends MenuCommand
{

	@Override
	public String getName()
	{
		return "宣伝する";
	}

	@Override
	public void workOnUiThread()
	{
		String str = "Android用Twitterクライアント「SmileEssence Lite」をチェック！\r\n http://smileessence.miz-hi.net/";
		PostSystem.setText(str).openPostPage();
	}

}
