package net.miz_hi.smileessence.permission;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.twitter.API;
import twitter4j.User;

public class PermissonChecker
{
	
	public static IPermission checkPermission(final Account account)
	{
		Future<User> future = MyExecutor.submit(new Callable<User>()
		{

			@Override
			public User call() throws Exception
			{
				return API.getUser(account, account.getUserId());
			}
		});
		try
		{
			User user = future.get();

			float ratio = (float)user.getFriendsCount() / (float)user.getFollowersCount();
			if(ratio > 2 && user.getFollowersCount() < 100)
			{
				return new PermissionBeginner();
			}
			else if(user.getFavouritesCount() < 10000)
			{
				return new PermissionIntermediate();
			}
			else
			{
				return new PermissionExpert();
			}
		}
		catch (Exception e)
		{
		}
		return new PermissionBeginner();
	}
}
