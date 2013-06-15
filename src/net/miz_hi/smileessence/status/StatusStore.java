package net.miz_hi.smileessence.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


import twitter4j.Status;

public class StatusStore
{
	private static ConcurrentHashMap<Long, StatusModel> statusesMap = new ConcurrentHashMap<Long, StatusModel>();
	private static CopyOnWriteArrayList<Long> favoriteList = new CopyOnWriteArrayList<Long>();
	private static CopyOnWriteArrayList<String> hashtagList = new CopyOnWriteArrayList<String>();
	private static CopyOnWriteArrayList<Long> readRetweetList = new CopyOnWriteArrayList<Long>();

	public static StatusModel put(Status status)
	{
		if (statusesMap.containsKey(status.getId()))
		{
			return statusesMap.get(status.getId());
		}
		StatusModel model = new StatusModel(status);
		if(status.isRetweet())
		{
			readRetweetList.add(model.statusId);
		}

		statusesMap.put(status.getId(), model);

		return model;
	}
	
	public static List<StatusModel> getList()
	{
		return new ArrayList<StatusModel>(statusesMap.values());
	}

	public static StatusModel get(long id)
	{
		return statusesMap.get(id);
	}

	public static StatusModel remove(long id)
	{
		return statusesMap.remove(id);
	}
	
	public static void putFavoritedStatus(long id)
	{
		favoriteList.add(id);
	}
	
	public static void removeFavoritedStatus(long id)
	{
		favoriteList.remove(id);
	}
	
	public static boolean isFavorited(long id)
	{
		return favoriteList.contains(id);
	}
	
	public static boolean isRead(long id)
	{
		int count = 0;
		for (Long l : readRetweetList)
		{
			if(id == l)
			{
				count++;
			}
		}
		return count > 1;
	}
	
	public static void putHashtag(String tag)
	{
		if(hashtagList.contains(tag))
		{
			return;
		}
		hashtagList.add(tag);
	}
	
	public static List<String> getHashtagList()
	{
		return hashtagList;
	}

	public static void clearCache()
	{
		statusesMap.clear();
		favoriteList.clear();
		hashtagList.clear();
		readRetweetList.clear();
	}
}
