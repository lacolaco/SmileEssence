package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.status.StatusModel;
import android.app.Activity;

public class StatusMenuAddReply extends StatusMenuItemBase
{

	public StatusMenuAddReply(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "ƒŠƒvƒ‰ƒCæ‚É’Ç‰Á";
	}

	@Override
	public void work()
	{
		MainActivity.getInstance().openTweetViewToReply(_model.screenName, -1, true);
	}

}
