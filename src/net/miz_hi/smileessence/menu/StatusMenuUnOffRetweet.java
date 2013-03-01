package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;

public class StatusMenuUnOffRetweet extends StatusMenuItemBase
{

	public StatusMenuUnOffRetweet(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return Client.getPermission().canUnOffRetweet() && !model.user.isProtected;
	}

	@Override
	public String getText()
	{
		return "”ñŒöŽ®RT";
	}

	@Override
	public void work()
	{
		final String text = " RT @" + model.screenName + ": " + model.text;
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				MainActivity.getInstance().openTweetViewToTweet(text);
			}
		}.post();
	}

}
