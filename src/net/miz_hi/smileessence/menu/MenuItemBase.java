package net.miz_hi.smileessence.menu;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.os.Handler;
import android.widget.Toast;

public abstract class MenuItemBase
{
	protected Activity _activity;
	protected DialogAdapter _adapter;
	private Handler _handler;

	public MenuItemBase(Activity activity, DialogAdapter adapter)
	{
		this._activity = activity;
		this._adapter = adapter;
		_handler = new Handler();
	}

	public abstract String getText();
	
	public boolean isVisible()
	{
		return true;
	}

	public void run()
	{
		_handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				_adapter.dispose();
				work();
			}
		}, 20);
	}

	public abstract void work();

	public void toast(String str)
	{
		Toast.makeText(_activity, str, Toast.LENGTH_SHORT).show();
	}

}
