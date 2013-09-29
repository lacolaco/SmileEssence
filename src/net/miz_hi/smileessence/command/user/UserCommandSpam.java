package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.notification.Notificator;
import net.miz_hi.smileessence.task.Task;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.TwitterException;

public class UserCommandSpam extends UserCommand implements IConfirmable
{

	public UserCommandSpam(String userName)
	{
		super(userName);
	}

	@Override
	public String getName()
	{
		return "スパム報告";
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
					API.spam(Client.getMainAccount(), userName);
				} 
				catch (TwitterException e)
				{
					e.printStackTrace();
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
					Notificator.info("スパム報告しました");
				}
				else
				{
					Notificator.alert("スパム報告失敗しました");
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