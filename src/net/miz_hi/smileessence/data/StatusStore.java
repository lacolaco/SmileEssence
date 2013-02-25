package net.miz_hi.smileessence.data;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

import twitter4j.Status;

public class StatusStore
{
	private static ConcurrentHashMap<Long, StatusModel> statusesMap = new ConcurrentHashMap<Long, StatusModel>();
	private static CopyOnWriteArrayList<Long> favoriteList = new CopyOnWriteArrayList<Long>();
	private static CopyOnWriteArrayList<String> hashtagList = new CopyOnWriteArrayList<String>();

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
	
	public static void putFavoritedStatus(long id)
	{
		favoriteList.add(id);
	}
	
	public static boolean isFavorited(long id)
	{
		return favoriteList.contains(id);
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
	}
}
