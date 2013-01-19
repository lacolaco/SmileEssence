package net.miz_hi.smileessence.model;

import java.util.HashMap;

public class EventStore
{
	private static HashMap<Integer, EventModel> eventsMap = new HashMap<Integer, EventModel>();
	private static int count = 0;

	public static void put(EventModel status)
	{
		eventsMap.put(count++, status);
	}

	public static EventModel get(int id)
	{
		return eventsMap.get(id);
	}

	public static EventModel remove(int id)
	{
		return eventsMap.remove(id);
	}
}
