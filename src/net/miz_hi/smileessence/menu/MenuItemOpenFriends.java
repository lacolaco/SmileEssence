package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.content.Intent;
import android.net.Uri;

public class MenuItemOpenFriends extends MenuItemBase
{

	public MenuItemOpenFriends(EventHandlerActivity activity, DialogAdapter adapter)
	{
		super(activity, adapter);
	}

	@Override
	public String getText()
	{
		return "ÉtÉHÉçÅ[Çå©ÇÈ(web)";
	}

	@Override
	public void work()
	{
		String url = "http://twitter.com/" + Client.getMainAccount().getScreenName() + "/following";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}

}
