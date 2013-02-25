package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;

public class StatusMenuHashtag extends StatusMenuItemBase
{
	
	private String hashtag;

	public StatusMenuHashtag(Activity activity, DialogAdapter adapter, StatusModel model, String hashtag)
	{
		super(activity, adapter, model);
		this.hashtag = hashtag;
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "#" + hashtag;
	}

	@Override
	public void work()
	{
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				MainActivity.getInstance().getTweetViewManager().appendText("#" + hashtag);
				MainActivity.getInstance().openTweetView();
			}
		}.post();
	}

}
