package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

@Deprecated
public class CommandOpenFriends extends MenuCommand
{

	private Activity activity;

	public CommandOpenFriends(Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "ÉtÉHÉçÅ[Çå©ÇÈ(web)";
	}

	@Override
	public void workOnUiThread()
	{
		String url = "http://twitter.com/" + Client.getMainAccount().getScreenName() + "/following";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

}
