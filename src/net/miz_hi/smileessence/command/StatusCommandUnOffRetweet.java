package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.view.TweetViewManager;

public class StatusCommandUnOffRetweet extends StatusCommand implements IHideable
{

	private static boolean isVisible = true;

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
		TweetViewManager manager = TweetViewManager.getInstance();
		manager.setText(text);
		manager.setCursor(0);
		manager.open();
	}

	@Override
	public boolean getIsVisible()
	{
		return isVisible && Client.getPermission().canUnOffRetweet() && !status.user.isProtected;
	}

	@Override
	public void setIsVisible(boolean value)
	{
		isVisible = value;
	}
}
