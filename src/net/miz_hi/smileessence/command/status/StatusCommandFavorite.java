package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandFavorite extends StatusCommand
{

	public StatusCommandFavorite(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "‚Ó‚Ÿ‚Ú‚é";
	}

	@Override
	public void workOnUiThread()
	{
		new AsyncFavoriteTask(status.statusId).addToQueue();
	}

}
