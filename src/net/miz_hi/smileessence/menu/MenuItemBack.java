package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import net.miz_hi.smileessence.core.EventHandlerActivity;
import net.miz_hi.smileessence.dialog.DialogAdapter;

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
	public	String getText()
	{
		return _text;
	}

	@Override
	public void work()
	{
		_activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				_adapter.setMenuItems(_list);
				_adapter.createMenuDialog(false).show();
			}
		});
	}

}
