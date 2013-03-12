package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.data.StatusModel;

public abstract class StatusCommand extends MenuCommand
{
	protected final StatusModel status;

	public StatusCommand(StatusModel status)
	{
		if(status == null)
		{
			this.status = StatusModel.getNullStatusModel();
		}
		else
		{
			this.status = status;
		}
	}

}
