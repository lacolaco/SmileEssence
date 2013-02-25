package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.app.Activity;
import android.view.View;

public class MenuItemBack extends MenuItemBase
{
	private String text;
	private List<MenuItemBase> list = new ArrayList<MenuItemBase>();
	private View[] titleView;

	public MenuItemBack(Activity activity, DialogAdapter adapter, String text, List<MenuItemBase> oldList)
	{
		super(activity, adapter);
		this.text = text;
		this.list.addAll(oldList);
		this.titleView = adapter.getTitleViews();
	}

	@Override
	public String getText()
	{
		return text;
	}

	@Override
	public void work()
	{
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				adapter.setMenuItems(list);
				adapter.setTitleViews(titleView);
				adapter.createMenuDialog(false).show();
			}
		});
	}

}
