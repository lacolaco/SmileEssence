package net.miz_hi.smileessence.command;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CommandOpenUrl extends MenuCommand
{

	private Activity activity;
	private String url;

	public CommandOpenUrl(Activity activity, String url)
	{
		this.activity = activity;
		this.url = url;
	}

	@Override
	public String getName()
	{
		return url;
	}

	@Override
	public void workOnUiThread()
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}
}
