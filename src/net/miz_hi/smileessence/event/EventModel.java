package net.miz_hi.smileessence.event;

import java.util.Date;

import twitter4j.User;

public abstract class EventModel implements Comparable<EventModel>
{

	protected Date date;
	public User source;

	protected EventModel(User sourceUser)
	{
		this.date = new Date();
		this.source = sourceUser;
	}
	
	@Override
	public int compareTo(EventModel another)
	{
		return this.date.compareTo(another.date);
	}
}
