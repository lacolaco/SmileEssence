package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.async.MyExecutor;
import net.miz_hi.smileessence.core.UiHandler;
import net.miz_hi.smileessence.dialog.DialogAdapter;
import net.miz_hi.smileessence.util.TwitterManager;
import android.app.Activity;
import android.widget.Toast;

public class UserMenuRemove extends UserMenuItemBase
{

	public UserMenuRemove(Activity activity, DialogAdapter adapter, String userName)
	{
		super(activity, adapter, userName);
	}

	@Override
	public boolean isVisible()
	{
		return true;
	}

	@Override
	public String getText()
	{
		return "ƒŠƒ€[ƒ”‚·‚é";
	}

	@Override
	public void work()
	{
		MyExecutor.execute(new Runnable()
		{
			
			@Override
			public void run()
			{
				if (TwitterManager.remove(Client.getMainAccount(), userName))
				{
					new UiHandler()
					{
						
						@Override
						public void run()
						{
							Toast.makeText(activity, "ƒŠƒ€[ƒ”‚µ‚Ü‚µ‚½", Toast.LENGTH_SHORT).show();
						}
					}.post();
				}				
			}
		});
	}

}
