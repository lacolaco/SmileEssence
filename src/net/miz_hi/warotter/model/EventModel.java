package net.miz_hi.warotter.model;

import twitter4j.User;
import net.miz_hi.warotter.util.EnumEventType;

public class EventModel
{
	public final EnumEventType type;
	public final User source;
	public final Object target;
	
	public EventModel(EnumEventType type, User source, Object target)
	{
		this.type = type;
		this.source = source;
		this.target = target;
	}
	
}
