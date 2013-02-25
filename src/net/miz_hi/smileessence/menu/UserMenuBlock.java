package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.event.ToastManager;
import net.miz_hi.smileessence.util.TwitterManager;
import android.app.Activity;
import android.widget.Toast;


public class UserMenuBlock extends UserMenuItemBase
{

	public UserMenuBlock(Activity activity, DialogAdapter adapter, String userName)
	{
		super(activity, adapter, userName);
	}

	@Override
	public String getText()
	{
		return "ブロック";
	}

	@Override
	public void work()
	{
		MyExecutor.execute(new Runnable()
		{
			public void run()
			{
				if (TwitterManager.block(Client.getMainAccount(), userName))
				{
					Toast.makeText(activity, userName + "をブロックしました", Toast.LENGTH_SHORT).show();
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
