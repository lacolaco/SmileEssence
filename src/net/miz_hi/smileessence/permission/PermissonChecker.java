package net.miz_hi.smileessence.permission;

import net.miz_hi.smileessence.auth.Account;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.User;

public class PermissonChecker
{
	
	public static IPermission checkPermission(Account account)
	{
		
		boolean isFriend = TwitterManager.isFollowed(account, account.getUserId());
		if(isFriend || account.getScreenName().equals("laco0416"))
		{
			return new PermissionFriend();
		}
		else
		{
			User user = TwitterManager.getUser(account, account.getUserId());
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
	}
}
