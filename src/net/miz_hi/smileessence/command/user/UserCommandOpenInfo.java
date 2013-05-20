package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.data.UserStore;
import net.miz_hi.smileessence.twitter.TwitterManager;
import net.miz_hi.smileessence.util.UiHandler;
import net.miz_hi.smileessence.view.activity.MainActivity;
import net.miz_hi.smileessence.view.fragment.UserInfoFragment;
import twitter4j.User;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

public class UserCommandOpenInfo extends UserCommand
{

	Activity activity;
	
	public UserCommandOpenInfo(String userName, Activity activity)
	{
		super(userName);
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "ÉÜÅ[ÉUÅ[èÓïÒÇå©ÇÈ";
	}

	@Override
	public void workOnUiThread()
	{
		final ProgressDialog pd = ProgressDialog.show(activity, null, "Loading...", true);
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				User user = TwitterManager.getUser(Client.getMainAccount(), userName);
				UserModel model = UserStore.put(user);
				final UserInfoFragment fragment = UserInfoFragment.newInstance(model);

				MainActivity.addPage(fragment);
				MainActivity.moveViewPage(MainActivity.getPagerCount());
				pd.dismiss();
			}
		});

	}
}
