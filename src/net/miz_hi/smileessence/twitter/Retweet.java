package net.miz_hi.smileessence.twitter;

import net.miz_hi.smileessence.auth.Account;
import twitter4j.TwitterException;

public class Retweet
{

	public static boolean retweet(Account account, long statusId)
	{
		try
		{
			TwitterManager.getTwitter(account).retweetStatus(statusId);
			return true;
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
