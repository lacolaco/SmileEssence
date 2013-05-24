package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.status.StatusModel;

public class StatusCommandFavorite extends StatusCommand
{

	public StatusCommandFavorite(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "お気に入りに追加";
	}

	@Override
	public void workOnUiThread()
	{
		new AsyncFavoriteTask(status.statusId).addToQueue();
	}

}
