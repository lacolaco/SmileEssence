package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.view.View;

public class ContentDialog
{

	private Activity activity;
	private String title;
	private View content;
	private String positive = "決定";
	private String negative = "キャンセル";
	private OnClickListener listener;

	public ContentDialog(Activity activity, String title)
	{
		this.activity = activity;
		this.title = title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public void setTextPositive(String textPositive)
	{
		this.positive = textPositive;
	}

	public void setTextNegative(String textNegative)
	{
		this.negative = textNegative;
	}
	
	public void setContentView(View content)
	{
		this.content = content;
	}

	public void setOnClickListener(OnClickListener listener)
	{
		this.listener = listener;
	}
	
	public AlertDialog create()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(activity);
		ad.setTitle(title);	
		ad.setView(content);
		ad.setPositiveButton(positive, listener);
		ad.setNegativeButton(negative, listener);
		return ad.create();
	}
}
