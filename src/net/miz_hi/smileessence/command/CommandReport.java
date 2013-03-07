package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.TweetViewManager;

public class CommandReport extends MenuCommand
{

	public CommandReport()
	{
	}

	@Override
	public String getName()
	{
		return "ìÒ‚ÖƒŒƒ|[ƒg‚ğ‘—‚é";
	}

	@Override
	public void workOnUiThread()
	{
		TweetViewManager.getInstance().setText("#SmileEssence @laco0416 ");
		TweetViewManager.getInstance().open();
	}

}
