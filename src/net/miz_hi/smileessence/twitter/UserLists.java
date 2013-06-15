package net.miz_hi.smileessence.twitter;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.Notifier;
import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.UserList;

public class UserLists
{
	
	public static List<UserList> getReadableLists(Account account)
	{
		List<UserList> lists = new ArrayList<UserList>();
		Twitter twitter = TwitterManager.getTwitter(account);
		try
		{
			lists.addAll(twitter.getUserLists(Client.getMainAccount().getUserId()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return lists;
	}
}
