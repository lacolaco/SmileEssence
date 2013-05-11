package net.miz_hi.smileessence.command.user;

import twitter4j.User;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.MainActivity;
import net.miz_hi.smileessence.view.UserTimelineFragment;

public class UserCommandOpenTimeline extends UserCommand
{

	public UserCommandOpenTimeline(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "タイムラインを開く";
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
				final UserModel model = UserStore.put(user);
				new UiHandler()
				{
					
					@Override
					public void run()
					{
						UserTimelineFragment fragment = UserTimelineFragment.newInstance(model);
						MainActivity.addPage(fragment);
						MainActivity.moveViewPage(MainActivity.getPagerCount());
					}
				}.post();

			}
		});
	}

}
