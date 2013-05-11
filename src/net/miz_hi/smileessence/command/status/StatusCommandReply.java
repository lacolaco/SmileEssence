package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandReply extends StatusCommand
{

	public StatusCommandReply(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "ï‘êM";
	}

	@Override
	public void workOnUiThread()
	{
		PostSystem.setReply(status.screenName, status.statusId).openPostPage();
	}

}
