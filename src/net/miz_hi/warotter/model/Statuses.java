package net.miz_hi.warotter.model;

import java.util.ArrayList;
import java.util.HashMap;

import twitter4j.Status;

public class Statuses
{
	private static HashMap<Long, Status> statuses = new HashMap<Long, Status>();
	
	public static void put(Status status)
	{
		statuses.put(status.getId(), status);
	}
	
	public static Status get(long id)
	{
		return statuses.get(id);
	}
	
	public static Status remove(long id)
	{
		return statuses.remove(id);
	}
}
