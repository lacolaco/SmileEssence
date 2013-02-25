package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;

public class TweetMenuHashtag extends TweetMenuItemBase
{
	
	private String hashtag;

	public TweetMenuHashtag(Activity activity, DialogAdapter adapter, TweetViewManager manager, String hashtag)
	{
		super(activity, adapter, manager);
		this.hashtag = hashtag;
	}

	@Override
	public String getText()
	{
		return "#" + hashtag;
	}

	@Override
	public void work()
	{
		manager.getEditTextTweet().append(" #" + hashtag);
	}

}
