package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;

public abstract class MenuDialog
{
	protected Activity activity;
	protected static LayoutInflater inflater;
	protected static Dialog dialog;
	
	public MenuDialog(Activity activity)
	{
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
	}
	
	public static Dialog getDialog()
	{
		return dialog;
	}

	public boolean isShowing()
	{
		if (dialog == null)
		{
			return false;
		}
		else
		{
			return dialog.isShowing();
		}
	}
	
	public abstract Dialog create();
	
	public static void dispose()
	{
		if (dialog != null)
		{
			dialog.dismiss();
		}
	}
}
