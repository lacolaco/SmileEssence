package net.miz_hi.smileessence.command.status;

import java.util.concurrent.Future;

import twitter4j.StatusUpdate;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavorite;
import net.miz_hi.smileessence.async.AsyncTweet;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.core.Notifier;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.twitter.TwitterManager;

public class StatusCommandUnOffFav extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandUnOffFav(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "非公式ふぁぼ";
	}

	@Override
	public void workOnUiThread()
	{
		String str = "@" + status.user.screenName + " っ★";
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(status.statusId);
		new AsyncFavorite(status.statusId).addToQueue();
		new AsyncTweet(update).addToQueue();		
	}
	

	@Override
	public boolean getDefaultVisibility()
	{
		return Client.getPermission().canWarotaRT();
	}

}
