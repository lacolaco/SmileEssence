package net.miz_hi.smileessence.command;

import twitter4j.StatusUpdate;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavoriteTask;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.data.StatusModel;

public class StatusCommandNanigaja extends StatusCommand implements IHideable, IConfirmable
{

	public StatusCommandNanigaja(StatusModel status)
	{
		super(status);
	}

	@Override
	public String getName()
	{
		return "な～にが○○じゃ";
	}

	@Override
	public void workOnUiThread()
	{
		String base = status.text;
		if (base.startsWith("."))
		{
			base = base.replaceFirst(".", "");
		}
		while (base.contains("@" + Client.getMainAccount().getScreenName()))
		{
			base = base.replaceFirst("@" + Client.getMainAccount().getScreenName(), "");
			base.trim();
		}
		String str = "な～にが" + base.trim() + "じゃ";
		long id = -1;
		if (status.isReply)
		{
			str = "@" + status.user.screenName + " " + str;
			id = status.statusId;
		}
		MyExecutor.submit(new AsyncFavoriteTask(status.statusId));
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(id);
		new AsyncTweetTask(update).addToQueue();
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return Client.getPermission().canWarotaRT();
	}

}
