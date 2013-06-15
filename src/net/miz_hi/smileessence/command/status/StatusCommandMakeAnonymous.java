package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.post.CommandMakeAnonymous;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandMakeAnonymous extends StatusCommand implements IHideable
{

	public StatusCommandMakeAnonymous(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "匿名にしてあげる";
	}

	@Override
	public void workOnUiThread()
	{
		String str = "？？？「" + status.text + "」";
		PostSystem.setText(str).openPostPage();
	}

}
