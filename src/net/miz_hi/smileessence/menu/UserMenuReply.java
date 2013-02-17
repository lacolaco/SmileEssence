package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;

public class UserMenuReply extends UserMenuItemBase
{

	public UserMenuReply(Activity activity, DialogAdapter adapter, String userName)
	{
		super(activity, adapter, userName);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "ƒŠƒvƒ‰ƒC‚ð‘—‚é";
	}

	@Override
	public void work()
	{
		MainActivity.getInstance().openTweetViewToReply(_userName, -1, false);
	}

}
