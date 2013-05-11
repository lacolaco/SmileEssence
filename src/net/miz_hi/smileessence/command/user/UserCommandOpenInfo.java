package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.UserInfoFragment;
import twitter4j.User;
import android.app.Activity;

public class UserCommandOpenInfo extends UserCommand
{

	public UserCommandOpenInfo(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "ÉÜÅ[ÉUÅ[èÓïÒÇå©ÇÈ";
	}

	@Override
	public void workOnUiThread()
	{
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				User user = TwitterManager.getUser(Client.getMainAccount(), userName);
				UserModel model = UserStore.put(user);
				UserInfoFragment fragment = UserInfoFragment.newInstance(model);
				MainActivity.addPage(fragment);
				MainActivity.moveViewPage(MainActivity.getPagerCount());
			}
		});

	}
}
