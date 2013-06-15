package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandUnOffRetweet extends StatusCommand implements IHideable
{

	public StatusCommandUnOffRetweet(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "非公式RT";
	}

	@Override
	public void workOnUiThread()
	{
		String text = " RT @" + status.screenName + ": " + status.text;
		PostSystem.clear().setText(text).setCursor(0).openPostPage();
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return Client.getPermission().canUnOffRetweet() && !status.user.isProtected;
	}
}
