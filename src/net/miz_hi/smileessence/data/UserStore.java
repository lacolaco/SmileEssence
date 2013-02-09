package net.miz_hi.smileessence.data;

import java.util.concurrent.ConcurrentHashMap;

import twitter4j.User;

public class UserStore
{

	private static ConcurrentHashMap<Long, UserModel> usersMap = new ConcurrentHashMap<Long, UserModel>();

	public static UserModel put(User user)
	{
		if (usersMap.containsKey(user.getId()))
		{
			usersMap.remove(user.getId());
		}
		UserModel model = new UserModel(user);
		usersMap.put(user.getId(), model);
		return model;
	}

	public static UserModel get(long id)
	{
		return usersMap.get(id);
	}

	public static UserModel remove(long id)
	{
		return usersMap.remove(id);
	}

	public static void clearCache()
	{
		usersMap.clear();
	}

}
