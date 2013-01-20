package net.miz_hi.smileessence.event;

import java.util.Date;

import net.miz_hi.smileessence.status.StatusModel;
import twitter4j.Status;
import twitter4j.User;

public class EventModel implements Comparable<EventModel>
{
	public Date date;
	public EnumEventType type;
	public User source;
	public StatusModel targetModel;

	public static EventModel createInstance(User source, EnumEventType type, Status target)
	{
		if(source == null)
		{
			return null;
		}
		else
		{
			return new EventModel(source, type, target);
		}
	}
	
	private EventModel(User source, EnumEventType type, Status target)
	{
		this.date = new Date();
		this.type = type;
		this.source = source;
		this.targetModel = StatusModel.createInstance(target);
	}

	@Override
	public int compareTo(EventModel another)
	{
		return this.date.compareTo(another.date);
	}

}
