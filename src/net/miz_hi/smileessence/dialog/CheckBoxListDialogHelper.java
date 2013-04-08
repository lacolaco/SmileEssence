package net.miz_hi.smileessence.dialog;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckBoxListDialogHelper
{
	
	private Activity activity;
	private String title;
	private OnClickListener onClicked;
	private CheckBoxItem[] items;

	public CheckBoxListDialogHelper(Activity activity)
	{
		this.activity = activity;
	}

	public void setTitle(String str)
	{
		this.title = str;
	}
	
	public void setOnClicked(OnClickListener listener)
	{
		this.onClicked = listener;
	}	
	
	public CheckBoxItem[] getItems()
	{
		return items;
	}

	public void setItems(CheckBoxItem... items)
	{
		this.items = items;
	}
	
	public Dialog createDialog()
	{
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_base_layout, null);
		LinearLayout titleLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_dialogTitle);
		LinearLayout itemsLinearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_dialogItems);
		
		TextView viewTitle = new TextView(activity);
		viewTitle.setTextSize(15);
		viewTitle.setTextColor(Client.getResource().getColor(R.color.White));
		viewTitle.setText(title);
		viewTitle.setPadding(10, 15, 0, 15);
		titleLinearLayout.addView(viewTitle);
		
		for (final CheckBoxItem item : items)
		{
			
			View viewItem = inflater.inflate(R.layout.checkboxitem_layout, null);
			CheckBox viewCheckBox = (CheckBox) viewItem.findViewById(R.id.checkBox_checkboxItem);
			viewCheckBox.setText(item.name);
			viewCheckBox.setChecked(item.value);
			viewCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					item.value = isChecked;
				}
			});
			
			itemsLinearLayout.addView(viewItem);
		}
		
		itemsLinearLayout.setClickable(true);
		
		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setPositiveButton("OK", onClicked);
		builder.setNegativeButton("ƒLƒƒƒ“ƒZƒ‹", onClicked);
		builder.setView(view);
		Dialog dialog = builder.create();
//		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		
//		LayoutParams lp = dialog.getWindow().getAttributes();
//		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
//		lp.width = (int) (metrics.widthPixels * 0.95);
//		lp.gravity = Gravity.CENTER;
		
		return dialog;
		
	}

	public static class CheckBoxItem
	{
		
		public String name;
		public boolean value;
		
		public CheckBoxItem(String name, boolean startValue)
		{
			this.name = name;
			this.value = startValue;
		}
	}

}
