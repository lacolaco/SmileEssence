package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.data.StatusStore;
import twitter4j.Status;
import twitter4j.User;

public abstract class StatusEvent extends EventModel
{

	public StatusModel targetModel;
	
	public StatusEvent(User source, Status targetStatus)
	{
		super(source);
		this.targetModel = StatusStore.put(targetStatus);
	}
}
