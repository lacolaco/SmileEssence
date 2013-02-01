package net.miz_hi.smileessence.menu;

import twitter4j.StatusUpdate;
import android.app.Activity;
import net.miz_hi.smileessence.Client;
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
		return "ƒƒƒ^®RT";
	}

	@Override
	public void work()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("ƒƒƒ^‚— RT @");
		builder.append(model.screenName);
		builder.append(": ");
		builder.append(model.text);
		StatusUpdate update = new StatusUpdate(builder.toString());
		update.setInReplyToStatusId(model.statusId);
		TwitterManager.tweet(Client.getMainAccount(), update);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

}
