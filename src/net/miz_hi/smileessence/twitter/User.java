package net.miz_hi.smileessence.twitter;

import net.miz_hi.smileessence.auth.Account;
import twitter4j.TwitterException;

public class User
{
	
	public static boolean follow(Account account, String name)
	{
		try
		{
			TwitterManager.getTwitter(account).createFriendship(name);
			return true;
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean remove(Account account, String name)
	{
		try
		{
			TwitterManager.getTwitter(account).destroyFriendship(name);
			return true;
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean block(Account account, String name)
	{
		try
		{
			TwitterManager.getTwitter(account).createBlock(name);
			return true;
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static boolean spam(Account account, String name)
	{
		try
		{
			TwitterManager.getTwitter(account).reportSpam(name);
			return true;
		}
		catch (TwitterException e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
