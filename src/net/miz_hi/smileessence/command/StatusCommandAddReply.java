package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.system.TweetSystem;
import net.miz_hi.smileessence.view.TweetView;

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
		TweetSystem.getInstance().addReply(status.screenName);
		ToastManager.show(status.screenName + "をリプライ先に追加しました");
	}
}
