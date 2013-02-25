package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

public abstract class MenuItemBase
{
	protected Activity activity;
	protected DialogAdapter adapter;
	private Handler handler;

	public MenuItemBase(Activity activity, DialogAdapter adapter)
	{
		this.activity = activity;
		this.adapter = adapter;
		handler = new Handler();
	}

	public abstract String getText();

	public boolean isVisible()
	{
		return true;
	}

	public void run()
	{
		handler.post(new Runnable()
		{
			@Override
			public void run()
			{
				adapter.dispose();
				work();
			}
		});
	}

	public abstract void work();

}
