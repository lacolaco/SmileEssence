package net.miz_hi.warotter.model;

import java.util.Date;

import net.miz_hi.warotter.util.EnumEventType;
import twitter4j.User;

public class EventModel implements Comparable<EventModel>
{
	public final Date date;
	public final EnumEventType type;
	public final User source;
	public final Object target;

	public EventModel(Date date, EnumEventType type, User source, Object target)
	{
		this.date = date;
		this.type = type;
		this.source = source;
		this.target = target;
	}

	@Override
	public int compareTo(EventModel another)
	{
		return this.date.compareTo(another.date);
	}

}
