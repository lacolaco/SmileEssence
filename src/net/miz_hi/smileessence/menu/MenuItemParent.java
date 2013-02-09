package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;

public class MenuItemParent extends MenuItemBase
{
	private String _text;
	private List<MenuItemBase> _list = new ArrayList<MenuItemBase>();

	public MenuItemParent(Activity activity, DialogAdapter adapter, String text, List<MenuItemBase> list)
	{
		super(activity, adapter);
		this._text = text;
		this._list.addAll(list);
	}

	@Override
	public String getText()
	{
		return _text + " >";
	}

	@Override
	public void work()
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!(_list.get(0) instanceof MenuItemBack))
					_list.add(0, new MenuItemBack(activity, adapter, "< –ß‚é", adapter.getList()));
				adapter.setMenuItems(_list);
				adapter.createMenuDialog(false).show();
			}
		});
	}
}
