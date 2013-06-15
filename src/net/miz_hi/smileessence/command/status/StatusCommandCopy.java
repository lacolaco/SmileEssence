package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavorite;
import net.miz_hi.smileessence.async.AsyncTweet;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.status.StatusModel;
import twitter4j.StatusUpdate;

public class StatusCommandCopy extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandCopy(StatusModel model)
	{
		super(model);
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return Client.getPermission().canCopyTweet();
	}

	@Override
	public String getName()
	{
		return "パクる";
	}

	@Override
	public void workOnUiThread()
	{
		StatusUpdate update = new StatusUpdate(status.text);
		update.setInReplyToStatusId(status.inReplyToStatusId);
		new AsyncTweet(update).addToQueue();
		new AsyncFavorite(status.statusId).addToQueue();
	}

}
