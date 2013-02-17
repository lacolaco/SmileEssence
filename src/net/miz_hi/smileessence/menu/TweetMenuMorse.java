package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.Morse;
import net.miz_hi.smileessence.view.TweetViewManager;
import android.app.Activity;

public class TweetMenuMorse extends TweetMenuBase
{

	public TweetMenuMorse(Activity activity, DialogAdapter adapter, TweetViewManager manager)
	{
		super(activity, adapter, manager);
	}

	@Override
	public String getText()
	{
		return "ÉÇÅ[ÉãÉXÇ…ïœä∑";
	}

	@Override
	public void work()
	{
		String str = manager.getEditTextTweet().getText().toString();
		String newStr = Morse.jaToMc(str);
		manager.getEditTextTweet().setText(newStr);
	}

}
