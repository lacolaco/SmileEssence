package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.system.TweetSystem;

public class StatusCommandAddReply extends StatusCommand
{

	public StatusCommandAddReply(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "リプライ先に追加";
	}

	@Override
	public void workOnUiThread()
	{
		TweetSystem.addReply(status.screenName);
		ToastManager.toast(status.screenName + "をリプライ先に追加しました");
	}
}
