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
		int cursor = manager.getEditTextTweet().getSelectionEnd();
		StringBuilder sb = new StringBuilder(manager.getEditTextTweet().getText().toString());
		sb.insert(cursor, "ƒƒƒ^‚—");
		manager.getEditTextTweet().setText(sb.toString());
		cursor = cursor + sb.length();
		if (cursor > manager.getEditTextTweet().getText().length())
		{
			cursor = manager.getEditTextTweet().getText().length();
		}
		manager.getEditTextTweet().setSelection(cursor);
	}

}
