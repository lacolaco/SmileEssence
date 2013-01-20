package net.miz_hi.smileessence.status;

import java.util.concurrent.ConcurrentHashMap;

import net.miz_hi.smileessence.Client;
import twitter4j.Status;
import twitter4j.UserMentionEntity;

public class StatusStore
{
	private static ConcurrentHashMap<Long, Status> statusesMap = new ConcurrentHashMap<Long, Status>();

	public static void put(Status status)
	{
		statusesMap.put(status.getId(), status);
	}

	public static Status get(long id)
	{
		return statusesMap.get(id);
	}

	public static Status remove(long id)
	{
		return statusesMap.remove(id);
	}
}
