package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.FrameLayout;

public class ConfirmDialog
{

	private Activity activity;
	private String textTitle;
	private String textPositive = "‚Í‚¢";
	private String textNegative = "‚¢‚¢‚¦";
	private OnClickListener listener;

	public ConfirmDialog(Activity activity, String title)
	{
		this.activity = activity;
		this.textTitle = title;
	}

	public void setTitle(String title)
	{
		this.textTitle = title;
	}

	public void setTextPositive(String textPositive)
	{
		this.textPositive = textPositive;
	}

	public void setTextNegative(String textNegative)
	{
		this.textNegative = textNegative;
	}

	public void setOnClickListener(OnClickListener listener)
	{
		this.listener = listener;
	}
	
	public AlertDialog createYesNoAlert()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(activity);
		ad.setTitle(textTitle);
		ad.setCancelable(false);
		ad.setPositiveButton(textPositive, listener);
		ad.setNegativeButton(textNegative, listener);
		return ad.create();
	}
	
	public static void show(Activity activity, String text, final Runnable onYes)
	{
		show(activity, text, onYes, null);
	}
	
	public static void show(Activity activity, String text, final Runnable onYes, final Runnable onNo)
	{
		ConfirmDialog helper = new ConfirmDialog(activity, text);
		FrameLayout frame = new FrameLayout(activity);
		OnClickListener listener = new OnClickListener()
		{
			
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				switch (which)
				{
					case DialogInterface.BUTTON_POSITIVE:
					{
						onYes.run();
						break;
					}
					case DialogInterface.BUTTON_NEGATIVE:
					{
						if(onNo != null)
						{
							onNo.run();
						}
						dialog.dismiss();
						break;
					}
				}
			}
		};
		helper.setOnClickListener(listener);
		helper.createYesNoAlert().show();
	}

}
