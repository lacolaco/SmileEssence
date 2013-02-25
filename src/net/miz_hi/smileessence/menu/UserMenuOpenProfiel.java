package net.miz_hi.smileessence.menu;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.TwitterManager;
import net.miz_hi.smileessence.view.UserActivity;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;

public class UserMenuOpenProfiel extends UserMenuItemBase
{

	public UserMenuOpenProfiel(Activity activity, DialogAdapter adapter, String userName)
	{
		super(activity, adapter, userName);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "ÉÜÅ[ÉUÅ[èÓïÒÇå©ÇÈ";
	}

	@Override
	public void work()
	{
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				final User user = TwitterManager.getUser(Client.getMainAccount(), userName);
				new UiHandler()
				{
					
					@Override
					public void run()
					{
						Intent intent = new Intent(activity, UserActivity.class);
						intent.putExtra("user_id", user.getId());
						activity.startActivity(intent);
					}
				}.post();
			}
		});
	}

}
