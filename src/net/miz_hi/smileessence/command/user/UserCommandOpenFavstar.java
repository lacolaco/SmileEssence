package net.miz_hi.smileessence.command.user;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class UserCommandOpenFavstar extends UserCommand
{

	private Activity activity;

	public UserCommandOpenFavstar(Activity activity, String userName)
	{
		super(userName);
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "Favstarを開く";
	}

	@Override
	public void workOnUiThread()
	{
		String url = "http://favstar.fm/users/" + userName + "/recent";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}
}
