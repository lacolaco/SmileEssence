package net.miz_hi.smileessence.data;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.User;

public class UserModel
{

	public final long userId;
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

	public boolean isFriend()
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
			return resp.get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean isFollower()
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
			return resp.get();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
