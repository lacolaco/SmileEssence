package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.view.TweetView;

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
		TweetView.getInstance().setText("#SmileEssence @laco0416 ");
		TweetView.getInstance().open();
	}

}
