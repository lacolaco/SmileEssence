package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;

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
