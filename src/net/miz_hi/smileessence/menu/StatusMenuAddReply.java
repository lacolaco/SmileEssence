package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.data.StatusModel;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.view.MainActivity;
import android.app.Activity;
import android.widget.Toast;

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
		return "リプライ先に追加";
	}

	@Override
	public void work()
	{
		MainActivity.getInstance().getTweetViewManager().addReply(model.screenName, -1, true);
		new UiHandler()
		{
			
			@Override
			public void run()
			{
				Toast.makeText(activity, model.screenName + "をリプライ先に追加しました", Toast.LENGTH_SHORT).show();
			}
		}.post();
	}

}
