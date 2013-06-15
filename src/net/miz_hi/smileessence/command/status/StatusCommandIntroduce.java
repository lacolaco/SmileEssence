package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandIntroduce extends StatusCommand implements IHideable
{

	public StatusCommandIntroduce(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "みんなに紹介する";
	}

	@Override
	public void workOnUiThread()
	{
		String str = status.name + " ( @" + status.screenName + " )";
		PostSystem.setText(str).setCursor(0).openPostPage();
	}

}
