package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;

public class MenuItemParent extends MenuItemBase
{
	private String text;
	private List<MenuItemBase> list = new ArrayList<MenuItemBase>();

	public MenuItemParent(EventHandlerActivity activity, DialogAdapter adapter, String text, List<MenuItemBase> list)
	{
		super(activity, adapter);
		this.text = text;
		this.list.addAll(list);
	}

	@Override
	public String getText()
	{
		return text + " >";
	}

	@Override
	public void work()
	{
		activity.runOnUiThread(new Runnable()
		{
			public void run()
			{
				if (!(list.get(0) instanceof MenuItemBack))
					list.add(0, new MenuItemBack(activity, adapter, "< –ß‚é", adapter.getList()));
				adapter.setMenuItems(list);
				adapter.createMenuDialog(false).show();
			}
		});
	}
}
