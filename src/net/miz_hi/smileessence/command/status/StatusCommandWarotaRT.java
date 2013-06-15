package net.miz_hi.smileessence.command.status;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavorite;
import net.miz_hi.smileessence.async.AsyncTweet;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.status.StatusModel;
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
		return "ワロタ式RT";
	}

	@Override
	public void workOnUiThread()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ワロタｗ RT @");
		builder.append(status.screenName);
		builder.append(": ");
		builder.append(status.text);
		StatusUpdate update = new StatusUpdate(builder.toString());
		update.setInReplyToStatusId(status.statusId);
		new AsyncTweet(update).addToQueue();
		new AsyncFavorite(status.statusId).addToQueue();
	}

	@Override
	public boolean getDefaultVisibility()
	{	
		return Client.getPermission().canWarotaRT() && !status.user.isProtected;
	}
}
