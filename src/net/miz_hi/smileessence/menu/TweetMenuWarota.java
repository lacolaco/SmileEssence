package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;

public class TweetMenuWarota extends TweetMenuItemBase
{

	public TweetMenuWarota(Activity activity, DialogAdapter adapter, TweetViewManager manager)
	{
		super(activity, adapter, manager);
	}

	@Override
	public String getText()
	{
		return "ƒƒƒ^‚—";
	}

	@Override
	public void work()
	{
		insertText("ƒƒƒ^‚—");
	}

}
