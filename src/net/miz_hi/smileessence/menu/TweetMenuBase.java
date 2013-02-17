package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;

public abstract class TweetMenuBase extends MenuItemBase
{
	protected TweetViewManager manager;

	public TweetMenuBase(Activity activity, DialogAdapter adapter, TweetViewManager manager)
	{
		super(activity, adapter);
		this.manager = manager;
	}
}
