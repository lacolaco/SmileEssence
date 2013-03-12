package net.miz_hi.smileessence.permission;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.Relationship;
import twitter4j.User;

public class PermissonChecker
{
	
	public static IPermission checkPermission(final Account account)
	{
		
		Future<Boolean> f1 = MyExecutor.submit(new Callable<Boolean>()
		{

			@Override
			public Boolean call() throws Exception
			{
				Relationship relationship = TwitterManager.getTwitter(account).showFriendship(account.getScreenName(), "laco0416");
				return relationship.isSourceFollowedByTarget();
			}
		});
		boolean isFriend = false;
		try
		{
			isFriend = f1.get();
		}
		catch (Exception e)
		{
		}
		if(isFriend || account.getScreenName().equals("laco0416"))
		{
			return new PermissionFriend();
		}
		else
		{
			Future<User> f2 = MyExecutor.submit(new Callable<User>()
			{

				@Override
				public User call() throws Exception
				{
					return TwitterManager.getUser(account, account.getUserId());
				}
			});
			try
			{
				User user = f2.get();

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
		}
		return new PermissionBeginner();
	}
}
