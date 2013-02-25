package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;

public class TweetMenuTemplate extends TweetMenuItemBase
{
	
	private String text;

	public TweetMenuTemplate(Activity activity, DialogAdapter adapter, TweetViewManager manager, String text)
	{
		super(activity, adapter, manager);
		this.text = text;
	}

	@Override
	public String getText()
	{
		return text;
	}

	@Override
	public void work()
	{
		insertText(text);
	}

}
