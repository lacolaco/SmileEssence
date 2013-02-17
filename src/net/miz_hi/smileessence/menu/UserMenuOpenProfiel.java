package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.data.UserModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.UserActivity;
import android.app.Activity;
import android.content.Intent;

public class UserMenuOpenProfiel extends MenuItemBase
{

	private UserModel user;

	public UserMenuOpenProfiel(Activity activity, DialogAdapter adapter, UserModel user)
	{
		super(activity, adapter);
		this.user = user;
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
		Intent intent = new Intent(activity, UserActivity.class);
		intent.putExtra("user_id", user.userId);
		activity.startActivity(intent);
	}

}
