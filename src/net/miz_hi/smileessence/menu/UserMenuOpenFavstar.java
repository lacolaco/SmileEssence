package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class UserMenuOpenFavstar extends UserMenuItemBase
{

	public UserMenuOpenFavstar(Activity activity, DialogAdapter adapter, String userName)
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
		return "ユーザーのFavstarを開く";
	}

	@Override
	public void work()
	{
		String url = "http://favstar.fm/users/" + _userName + "/recent";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		_activity.startActivity(intent);
	}

}
