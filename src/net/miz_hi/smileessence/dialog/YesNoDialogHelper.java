package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class YesNoDialogHelper
{

	private Activity activity;
	private String textTitle;
	private String textPositive = "‚Í‚¢";
	private String textNegative = "‚¢‚¢‚¦";
	private OnClickListener listener;
	private LayoutInflater layoutInflater;
	private View dialogView;
	private View contentView;

	public YesNoDialogHelper(Activity activity, String title)
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

	public void setContentView(View view)
	{
		this.contentView = view;
	}

	public AlertDialog createYesNoAlert()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(activity);
		ad.setTitle(textTitle);
		ad.setView(contentView);
		ad.setPositiveButton(textPositive, listener);
		ad.setNegativeButton(textNegative, listener);
		return ad.create();
	}
	
	public static void show(Activity activity, String title, String text, final Runnable onYes)
	{
		YesNoDialogHelper helper = new YesNoDialogHelper(activity, title);
		TextView viewText = new TextView(activity);
		viewText.setText(text);
		viewText.setTextColor(Client.getColor(R.color.White));
		viewText.setPadding(10, 20, 0, 20);
		helper.setContentView(viewText);
		helper.setOnClickListener(new OnClickListener()
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
						dialog.dismiss();
						break;
					}
				}
			}
		});
		helper.createYesNoAlert().show();
	}

}
