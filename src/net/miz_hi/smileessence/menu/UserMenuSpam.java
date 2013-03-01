package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;
import android.app.Activity;
import android.widget.Toast;


public class UserMenuSpam extends UserMenuItemBase
{

	public UserMenuSpam(Activity activity, DialogAdapter adapter, String userName)
	{
		super(activity, adapter, userName);
	}

	@Override
	public String getText()
	{
		return "スパム報告";
	}

	@Override
	public void work()
	{
		MyExecutor.execute(new Runnable()
		{
			public void run()
			{
				if (TwitterManager.spam(Client.getMainAccount(), userName))
				{
					new UiHandler()
					{
						
						@Override
						public void run()
						{
							Toast.makeText(activity, userName + "をスパム報告しました", Toast.LENGTH_SHORT).show();
						}
					}.post();
				}
			}
		});
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

}
