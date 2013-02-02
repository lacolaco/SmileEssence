package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.activity.MainActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.TwitterManager;
import twitter4j.User;
import android.app.Activity;
import android.widget.Toast;

public class UserMenuRemove extends UserMenuItemBase
{

	public UserMenuRemove(Activity activity, DialogAdapter adapter, String userName)
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
		return "ƒŠƒ€[ƒ”‚·‚é";
	}

	@Override
	public void work()
	{
		if(TwitterManager.remove(Client.getMainAccount(), _userName))
		{
			Toast.makeText(_activity, "ƒŠƒ€[ƒ”‚µ‚Ü‚µ‚½", Toast.LENGTH_SHORT).show();
		}
	}

}
