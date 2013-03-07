package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.data.StatusModel;

public abstract class StatusCommand extends MenuCommand
{
	protected StatusModel status;

	public StatusCommand(StatusModel status)
	{
		this.status = status;
	}

}
