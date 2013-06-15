package net.miz_hi.smileessence.command.status;

import twitter4j.TwitterException;
import net.miz_hi.smileessence.async.AsyncUnFavorite;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandUnfavorite extends StatusCommand implements IHideable
{

	public StatusCommandUnfavorite(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "お気に入りを削除する";
	}
	
	@Override
	public void workOnUiThread()
	{
		new AsyncUnFavorite(status.statusId).addToQueue();
	}

}