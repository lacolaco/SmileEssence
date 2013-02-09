package net.miz_hi.smileessence.data;

import java.util.concurrent.ConcurrentHashMap;

import twitter4j.Status;

public class StatusStore
{
	private static ConcurrentHashMap<Long, StatusModel> statusesMap = new ConcurrentHashMap<Long, StatusModel>();

	public static StatusModel put(Status status)
	{
		if (statusesMap.containsKey(status.getId()))
		{
			statusesMap.remove(status.getId());
		}
		StatusModel model = new StatusModel(status);
		statusesMap.put(status.getId(), model);
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

	public static void clearCache()
	{
		statusesMap.clear();
	}
}
