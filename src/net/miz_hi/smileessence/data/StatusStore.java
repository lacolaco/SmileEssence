package net.miz_hi.smileessence.data;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import twitter4j.Status;

public class StatusStore
{
	private static ConcurrentHashMap<Long, StatusModel> statusesMap = new ConcurrentHashMap<Long, StatusModel>();
	private static ConcurrentLinkedQueue<Long> favoriteList = new ConcurrentLinkedQueue<Long>();

	public static StatusModel put(Status status)
	{
		if (statusesMap.containsKey(status.getId()))
		{
			statusesMap.remove(status.getId());
		}
		StatusModel model = new StatusModel(status);
		if(status.isRetweet())
		{
			statusesMap.put(status.getRetweetedStatus().getId(), model);
		}
		else
		{
			statusesMap.put(status.getId(), model);
		}
		return model;
	}

	public static StatusModel get(long id)
	{
		return statusesMap.get(id);
	}

	public static StatusModel remove(long id)
	{
		return statusesMap.remove(id);
	}
	
	public static boolean putFavoritedStatus(long id)
	{
		return favoriteList.offer(id);
	}
	
	public static boolean isFavorited(long id)
	{
		return favoriteList.contains(id);
	}

	public static void clearCache()
	{
		statusesMap.clear();
	}
}
