package net.miz_hi.smileessence.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class CheckBoxListDialog
{
	
	private Activity activity;
	private String title;
	private OnClickListener onClicked;
	private CheckBoxItem[] items;

	public CheckBoxListDialog(Activity activity)
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
		ScrollView scroll = new ScrollView(activity);
		LinearLayout linearLayout = new LinearLayout(activity);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		p.setMargins(0, 10, 0, 10);
		
		for (final CheckBoxItem item : items)
		{
			CheckBox checkbox = new CheckBox(activity);
			checkbox.setLayoutParams(p);
			checkbox.setText(item.name);
			checkbox.setChecked(item.value);
			checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					item.value = isChecked;
				}
			});
			linearLayout.addView(checkbox);
		}
		scroll.addView(linearLayout);
		
		ContentDialog dialog = new ContentDialog(activity, title);
		dialog.setContentView(scroll);
		dialog.setOnClickListener(onClicked);
		return dialog.create();
		
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
