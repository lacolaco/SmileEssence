package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.view.TweetViewManager;

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
		TweetViewManager.getInstance().addReply(status.screenName);
		ToastManager.getInstance().toast(status.screenName + "をリプライ先に追加しました");
	}
}
