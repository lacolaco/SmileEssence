package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;

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
		_activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if (!(_list.get(0) instanceof MenuItemBack))
					_list.add(0, new MenuItemBack(_activity, _adapter, "< –ß‚é", _adapter.getList()));
				_adapter.setMenuItems(_list);
				_adapter.createMenuDialog(false).show();
			}
		});
	}
}
