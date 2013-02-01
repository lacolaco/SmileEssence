package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;

public class YesNoDialogHelper
{

	private Activity activity;
	private String title;
	private OnClickListener listener;
	private LayoutInflater layoutInflater;
	private View dialogView;
	private View contentView;

	public YesNoDialogHelper(Activity activity, String title)
	{
		this.activity = activity;
		this.title = title;
	}

	public void setTitle(String title)
	{
		this.title = title;
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
		ad.setTitle(title);
		ad.setView(contentView);
		ad.setPositiveButton("‚Í‚¢", listener);
		ad.setNegativeButton("‚¢‚¢‚¦", listener);
		return ad.create();
	}

}
