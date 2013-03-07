package net.miz_hi.smileessence.command;

import net.miz_hi.smileessence.Client;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

/**
 * @deprecated This command is not work well now.
 */
@Deprecated
public class CommandOpenFollowers extends MenuCommand
{

	private Activity activity;

	public CommandOpenFollowers(Activity activity)
	{
		this.activity = activity;
	}

	@Override
	public String getName()
	{
		return "ÉtÉHÉçÉèÅ[Çå©ÇÈ(web)";
	}

	@Override
	public void workOnUiThread()
	{
		String url = "http://twitter.com/" + Client.getMainAccount().getScreenName() + "/followers";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

}
