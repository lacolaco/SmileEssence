package net.miz_hi.smileessence.command.status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncFavorite;
import net.miz_hi.smileessence.async.AsyncTweet;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.status.StatusModel;
import twitter4j.StatusUpdate;

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
		if(base.startsWith("@" + Client.getMainAccount().getScreenName()))
		{
			base.replaceFirst(Client.getMainAccount().getScreenName(), status.user.screenName);
		}
		String str = "な～にが" + base.trim() + "じゃ";
		long id = status.statusId;
		new AsyncFavorite(status.statusId).addToQueue();
		StatusUpdate update = new StatusUpdate(str);
		update.setInReplyToStatusId(id);
		new AsyncTweet(update).addToQueue();
	}

	@Override
	public boolean getDefaultVisibility()
	{
		return Client.getPermission().canWarotaRT();
	}

}
