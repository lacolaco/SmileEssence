package net.miz_hi.smileessence.command.user;

import net.miz_hi.smileessence.view.UserActivity;
import android.app.Activity;
import android.content.Intent;

public class UserCommandOpenProfiel extends UserCommand
{

	private Activity activity;

	public UserCommandOpenProfiel(Activity activity, String userName)
	{
		super(userName);
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "ƒ†[ƒU[î•ñ‚ğŒ©‚é";
	}

	@Override
	public void workOnUiThread()
	{
		Intent intent = new Intent(activity, UserActivity.class);
		intent.putExtra("screen_name", userName);
		activity.startActivity(intent);
	}
}
