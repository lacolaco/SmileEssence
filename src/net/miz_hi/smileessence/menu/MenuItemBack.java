package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;

public class MenuItemBack extends MenuItemBase
{
	private String _text;
	private List<MenuItemBase> _list = new ArrayList<MenuItemBase>();

	public MenuItemBack(Activity activity, DialogAdapter adapter, String text, List<MenuItemBase> listOld)
	{
		super(activity, adapter);
		this._text = text;
		this._list.addAll(listOld);
	}

	@Override
	public String getText()
	{
		return _text;
	}

	@Override
	public void work()
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				adapter.setMenuItems(_list);
				adapter.createMenuDialog(false).show();
			}
		});
	}

}
