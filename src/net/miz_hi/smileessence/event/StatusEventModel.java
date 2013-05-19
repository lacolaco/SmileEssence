package net.miz_hi.smileessence.event;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.status.StatusModel;

public abstract class StatusEventModel extends EventModel
{

	public StatusModel targetModel;
	
	public StatusEventModel(UserModel retweeter, StatusModel status)
	{
		super(retweeter);
		this.targetModel = status;
	}
}
