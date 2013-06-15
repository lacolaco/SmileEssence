package net.miz_hi.smileessence.twitter;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.Notifier;
import twitter4j.TwitterException;

public class Favorite
{
	
	public static boolean favorite(Account account, long statusId)
	{
		try
		{
			TwitterManager.getTwitter(account).createFavorite(statusId);
			return true;
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean unfavorite(Account account, long statusId)
	{
		try
		{
			TwitterManager.getTwitter().destroyFavorite(statusId);
			return true;
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
