package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class MenuItemOpenFavstar extends MenuItemBase
{

	public MenuItemOpenFavstar(Activity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "Favstar‚ðŠJ‚­";
	}

	@Override
	public void work()
	{
		String url = "http://favstar.fm/users/" + Client.getMainAccount().getScreenName() + "/recent";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

}
