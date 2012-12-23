package net.miz_hi.warotter.model;

import java.util.HashMap;

import net.miz_hi.warotter.Warotter;

import twitter4j.Status;
import twitter4j.UserMentionEntity;

public class StatusStore
{
	private static HashMap<Long, Status> statusesMap = new HashMap<Long, Status>();

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

	public static boolean isReply(long id)
	{
		Status st = statusesMap.get(id);
		for (UserMentionEntity ume : st.getUserMentionEntities())
		{
			if (ume.getScreenName().equals(Warotter.getAccount().screenName))
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isMine(long id)
	{
		return statusesMap.get(id).getUser().getId() == Warotter.getAccount().userId;
	}
}
