package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class CommandOpenFavstar extends MenuCommand
{

	private Activity activity;

	public CommandOpenFavstar(Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "Favstar‚ðŠJ‚­(web)";
	}

	@Override
	public void workOnUiThread()
	{
		String url = "http://favstar.fm/users/" + Client.getMainAccount().getScreenName() + "/recent";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

}
