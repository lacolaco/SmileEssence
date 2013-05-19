package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;
import twitter4j.StatusUpdate;

public class StatusCommandWarotaRT extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandWarotaRT(StatusModel model)
	{
		super(model);
	}

	@Override
	public String getName()
	{
		return "ÉèÉçÉ^éÆRT";
	}

	@Override
	public void workOnUiThread()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ÉèÉçÉ^Çó RT @");
		builder.append(status.screenName);
		builder.append(": ");
		builder.append(status.text);
		StatusUpdate update = new StatusUpdate(builder.toString());
		update.setInReplyToStatusId(status.statusId);
		new AsyncTweetTask(update).addToQueue();
		new AsyncFavoriteTask(status.statusId).addToQueue();
	}

	@Override
	public boolean getDefaultVisibility()
	{	
		return Client.getPermission().canWarotaRT() && !status.user.isProtected;
	}
}
