package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class UserCommandFollow extends UserCommand implements IConfirmable
{

	public UserCommandFollow(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "フォローする";
	}

	@Override
	public void workOnUiThread()
	{
		new Task<Boolean>()
		{

			@Override
			public Boolean call()
			{
				try
				{
					API.follow(Client.getMainAccount(), userName);
				}
				catch(TwitterException e)
				{
					return false;
				}
				return true;
			}

			@Override
			public void onPreExecute()
			{
			}

			@Override
			public void onPostExecute(Boolean result)
			{
				if(result)
				{
					Notificator.info("フォローしました");
				}
				else
				{
					Notificator.alert("フォロー失敗しました");
				}
			}

		}.callAsync();
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return !Client.getMainAccount().getScreenName().equals(userName);
	}

}