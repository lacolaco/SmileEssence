package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class UserMenuOpenPage extends UserMenuItemBase
{

	public UserMenuOpenPage(Activity activity, DialogAdapter adapter, String userName)
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
		return "ユーザーページを開く";
	}

	@Override
	public void work()
	{
		String userPage = "http://twitter.com/" + _userName;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userPage));
		activity.startActivity(intent);
	}

}
