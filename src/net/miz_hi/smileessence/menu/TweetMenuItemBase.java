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
	
	protected final void insertText(String str)
	{
		int cursor = manager.getEditTextTweet().getSelectionEnd();
		StringBuilder sb = new StringBuilder(manager.getEditTextTweet().getText().toString());
		sb.insert(cursor, str);
		manager.getEditTextTweet().setText(sb.toString());
		cursor = cursor + sb.length();
		if (cursor > manager.getEditTextTweet().getText().length())
		{
			cursor = manager.getEditTextTweet().getText().length();
		}
		manager.getEditTextTweet().setSelection(cursor);
	}
}
