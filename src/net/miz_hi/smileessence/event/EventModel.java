package net.miz_hi.smileessence.event;

import java.util.Date;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.view.ITimelineItem;

public abstract class EventModel implements Comparable<EventModel>, ITimelineItem
{

	protected Date date;
	public UserModel source;

	protected EventModel(UserModel retweeter)
	{
		this.date = new Date();
		this.source = retweeter;
	}
	
	public abstract String getText();
	
	@Override
	public int compareTo(EventModel another)
	{
		return this.date.compareTo(another.date);
	}
}
