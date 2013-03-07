package net.miz_hi.smileessence.command;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class UserCommandOpenPage extends UserCommand
{

	private Activity activity;

	public UserCommandOpenPage(Activity activity, String userName)
	{
		super(userName);
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "ユーザーページを開く";
	}

	@Override
	public void workOnUiThread()
	{
		String userPage = "http://twitter.com/" + userName;
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userPage));
		activity.startActivity(intent);
	}

	@Override
	public boolean getIsVisible()
	{
		return true;
	}

}
