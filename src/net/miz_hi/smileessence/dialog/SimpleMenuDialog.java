package net.miz_hi.smileessence.dialog;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.menu.MenuListAdapter;
import net.miz_hi.smileessence.preference.EnumPreferenceKey.EnumValueType;
import net.miz_hi.smileessence.preference.PreferenceHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;

public abstract class SimpleMenuDialog extends MenuDialog
{
	
	protected View titleView;
	protected String title;

	public SimpleMenuDialog(Activity activity)
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

	public abstract List<ICommand> getMenuList();
	
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
		
		List<ICommand> list1 = getMenuList();
		List<ICommand> list2 = new ArrayList<ICommand>();
		
		for(ICommand command : list1)
		{
			boolean isEnabled = true;

			if(command instanceof IHideable)
			{
				PreferenceHelper pref = Client.getPreferenceHelper();
				isEnabled = pref.getPreferenceValue(command.getClass().getSimpleName(), EnumValueType.BOOLEAN, false);
			}

			if(command.getDefaultVisibility() && isEnabled)
			{
				list2.add(command);
			}
		}

		ListView listview = new ListView(activity);
		MenuListAdapter adapter = new MenuListAdapter(activity);
		adapter.addAll(list2);
		adapter.forceNotifyAdapter();
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
