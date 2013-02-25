package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;

public abstract class TweetMenuItemBase extends MenuItemBase
{
	protected TweetViewManager manager;

	public TweetMenuItemBase(Activity activity, DialogAdapter adapter, TweetViewManager manager)
	{
		super(activity, adapter);
		this.manager = manager;
	}
}
