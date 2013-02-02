package net.miz_hi.smileessence.menu;

import twitter4j.StatusUpdate;
import android.app.Activity;
import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.AsyncTweetTask;
import net.miz_hi.smileessence.async.ConcurrentAsyncTaskHelper;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import net.miz_hi.smileessence.util.TwitterManager;

public class StatusMenuWarotaRT extends StatusMenuItemBase
{

	public StatusMenuWarotaRT(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public String getText()
	{
		return "ÉèÉçÉ^éÆRT";
	}

	@Override
	public void work()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ÉèÉçÉ^Çó RT @");
		builder.append(_model.screenName);
		builder.append(": ");
		builder.append(_model.text);
		StatusUpdate update = new StatusUpdate(builder.toString());
		update.setInReplyToStatusId(_model.statusId);
		ConcurrentAsyncTaskHelper.addAsyncTask(new AsyncTweetTask(update));
	}

	@Override
	public boolean isVisible()
	{
		return false;
	}

}
