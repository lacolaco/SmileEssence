package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.status.StatusModel;

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
