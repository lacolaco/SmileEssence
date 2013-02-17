package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;

public class StatusMenuAddReply extends StatusMenuItemBase
{

	public StatusMenuAddReply(Activity activity, DialogAdapter adapter, StatusModel model)
	{
		super(activity, adapter, model);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "���v���C��ɒǉ�";
	}

	@Override
	public void work()
	{
		MainActivity.getInstance().openTweetViewToReply(model.screenName, -1, true);
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				MainActivity.getInstance().closeTweetView();
			}
		}.postDelayed(800);
	}

}