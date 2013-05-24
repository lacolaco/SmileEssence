package net.miz_hi.smileessence.dialog;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.menu.ExpandMenuListAdapter;
import net.miz_hi.smileessence.preference.EnumPreferenceKey.EnumValueType;
import net.miz_hi.smileessence.preference.PreferenceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public abstract class ExpandMenuDialog extends MenuDialog
{
	
	protected View titleView;
	protected String title;

	public ExpandMenuDialog(Activity activity)
	{
		super(activity);
	}
	
	public void setTitle(View view)
	{
		titleView = view;
	}
	
	public View getTitleView()
	{
		return titleView; 
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}

	public abstract List<List<ICommand>> getLists();
	
	public abstract List<String> getGroups();
	
	public Dialog create()
	{
		dispose();
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		if(titleView == null)
		{
			builder.setTitle(title);
		}
		else
		{
			builder.setCustomTitle(titleView);
		}
		
		List<List<ICommand>> list1 = getLists();
		List<List<ICommand>> list2 = new ArrayList<List<ICommand>>();
		
		for(int i = 0; i < list1.size(); i++)
		{
			List<ICommand> list = list1.get(i);
			List<ICommand> newList = new ArrayList<ICommand>();
			for(ICommand command : list)
			{
				boolean isEnabled = true;
				
				if(command instanceof IHideable)
				{
					PreferenceHelper pref = Client.getPreferenceHelper();
					isEnabled = pref.getPreferenceValue(command.getClass().getSimpleName(), EnumValueType.BOOLEAN, false);
				}
				
				if(command.getDefaultVisibility() && isEnabled)
				{
					newList.add(command);
				}
			}
			list2.add(i, newList);
		}
		
		ExpandableListView listview = new ExpandableListView(activity);
		listview.setOnGroupClickListener(new OnGroupClickListener()
		{
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id)
			{
				parent.smoothScrollToPosition(groupPosition);  
		        return false;
			}
		});
		
		ExpandMenuListAdapter adapter = new ExpandMenuListAdapter(activity, getGroups(), list2);
		listview.setAdapter(adapter);
		builder.setView(listview);
		dialog = builder.create();
		LayoutParams lp = dialog.getWindow().getAttributes();
		DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
		lp.width = (int) (metrics.widthPixels * 0.9);
		lp.gravity = Gravity.CENTER;
		lp.height = (int) (metrics.heightPixels * 0.8);
		
		return dialog;
	}
}
