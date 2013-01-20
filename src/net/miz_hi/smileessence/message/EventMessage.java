package net.miz_hi.smileessence.message;

import net.miz_hi.smileessence.core.Message;
import net.miz_hi.smileessence.event.EventModel;

public class EventMessage implements Message
{

	public EventModel model;
	
	public EventMessage(EventModel model)
	{
		this.model = model;
	}
}
