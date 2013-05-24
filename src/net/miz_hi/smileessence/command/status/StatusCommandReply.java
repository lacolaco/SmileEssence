package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.status.StatusModel;
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
		return "返信";
	}

	@Override
	public void workOnUiThread()
	{
		PostSystem.setReply(status.screenName, status.statusId).openPostPage();
	}

}
