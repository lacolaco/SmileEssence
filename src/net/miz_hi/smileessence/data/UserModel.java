package net.miz_hi.smileessence.data;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.twitter.TwitterManager;
import twitter4j.User;

public class UserModel
{

	public long userId;
	public String screenName;
	public String name;
	public String homePageUrl;
	public String location;
	public String description;
	public String iconUrl;
	public int statusCount;
	public int friendCount;
	public int followerCount;
	public int favoriteCount;
	public Date createdAt;
	public boolean isProtected;
	private Boolean isFriend = null;
	private Boolean isFollower = null;

	private UserModel(){}
	
	public UserModel(User user)
	{
		userId = user.getId();
		updateData(user);
	}

	public UserModel updateData(User user)
	{
		screenName = user.getScreenName();
		name = user.getName();
		homePageUrl = user.getURL();
		location = user.getLocation();
		description = user.getDescription();
		iconUrl = user.getProfileImageURL();
		statusCount = user.getStatusesCount();
		friendCount = user.getFriendsCount();
		followerCount = user.getFollowersCount();
		favoriteCount = user.getFavouritesCount();
		createdAt = user.getCreatedAt();
		isProtected = user.isProtected();
		IconCaches.checkIconCache(UserModel.this);
		return this;
	}

	public User getUser()
	{
		Future<User> resp = MyExecutor.submit(new Callable<User>()
		{

			@Override
			public User call()
			{
				return TwitterManager.getUser(Client.getMainAccount(), userId);
			}
		});
		try
		{
			return resp.get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public boolean isMe()
	{
		return userId == Client.getMainAccount().getUserId();
	}

	public boolean isFriend(boolean force)
	{
		if(isFriend == null || force)
		{
			Future<Boolean> resp = MyExecutor.submit(new Callable<Boolean>()
					{

						@Override
						public Boolean call() throws Exception
						{
							return TwitterManager.isFollowing(Client.getMainAccount(), userId);
						}
					});
					try
					{
						isFriend = resp.get();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						return false;
					}
		}	

		return isFriend.booleanValue();
	}

	public boolean isFollower(boolean force)
	{
		if(isFollower == null || force)
		{
			Future<Boolean> resp = MyExecutor.submit(new Callable<Boolean>()
					{

				@Override
				public Boolean call() throws Exception
				{
					return TwitterManager.isFollowed(Client.getMainAccount(), userId);
				}
					});
			try
			{
				isFollower = resp.get();
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		return isFollower.booleanValue();
	}

	public static UserModel getNullUserModel()
	{
		UserModel user = new UserModel();
		user.screenName = "";
		user.name = "";
		user.homePageUrl = "";
		user.location = "";
		user.description = "";
		user.iconUrl = "";
		user.statusCount = 0;
		user.friendCount = 0;
		user.followerCount = 0;
		user.favoriteCount = 0;
		user.createdAt = new Date();
		user.isProtected = false;
		return user;
	}
}
