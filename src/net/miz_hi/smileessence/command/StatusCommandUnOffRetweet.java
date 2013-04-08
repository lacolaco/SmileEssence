package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.view.TweetView;

public class StatusCommandUnOffRetweet extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandUnOffRetweet(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "”ñŒöŽ®RT";
	}

	@Override
	public void workOnUiThread()
	{
		String text = " RT @" + status.screenName + ": " + status.text;
		TweetSystem.setText(text);
		TweetSystem.setCursor(0);
		TweetView.open();
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return Client.getPermission().canUnOffRetweet() && !status.user.isProtected;
	}
}
