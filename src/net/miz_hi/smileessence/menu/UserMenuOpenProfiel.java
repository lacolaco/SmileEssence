package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.activity.UserActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import twitter4j.User;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class UserMenuOpenProfiel extends UserMenuItemBase
{

	public UserMenuOpenProfiel(Activity activity, DialogAdapter adapter, String userName)
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
		return "ƒ†[ƒU[î•ñ‚ğŒ©‚é";
	}

	@Override
	public void work()
	{
		Intent intent = new Intent(_activity, UserActivity.class);
		intent.putExtra("name", _userName);
		_activity.startActivity(intent);
	}

}
