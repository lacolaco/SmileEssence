package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class MenuItemOpenFollowers extends MenuItemBase
{

	public MenuItemOpenFollowers(Activity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "ƒtƒHƒƒ[‚ğŒ©‚é(web)";
	}

	@Override
	public void work()
	{
		String url = "http://twitter.com/" + Client.getMainAccount().getScreenName() + "/followers";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

}
