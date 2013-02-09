package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class StatusMenuOpenUrl extends StatusMenuItemBase
{

	private String url;

	public StatusMenuOpenUrl(Activity activity, DialogAdapter adapter, StatusModel model, String url)
	{
		super(activity, adapter, model);
		this.url = url;
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return url;
	}

	@Override
	public void work()
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		activity.startActivity(intent);
	}
}
