package net.miz_hi.warotter.menuitem;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.warotter.core.EventHandlerActivity;
import net.miz_hi.warotter.dialog.DialogAdapter;
import net.miz_hi.warotter.dialog.OptionMenuAdapter;

import android.app.Activity;

public class MenuItemBack extends MenuItemBase
{
	String text;
	List<MenuItemBase> list = new ArrayList<MenuItemBase>();

	public MenuItemBack(EventHandlerActivity activity, DialogAdapter adapter, String text, List<MenuItemBase> listOld)
	{
		super(activity, adapter);
		this.text = text;
		this.list.addAll(listOld);
	}

	@Override
	public	String getText()
	{
		return text;
	}

	@Override
	public void work()
	{
		activity.runOnUiThread(new Runnable()
		{
			public void run()
			{
				adapter.setMenuItems(list);
				adapter.createMenuDialog().show();
			}
		});
	}

}
