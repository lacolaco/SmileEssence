package net.miz_hi.smileessence.command;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.dialog.DialogAdapter;
import android.view.View;

public class CommandMenuBack extends MenuCommand
{

	private DialogAdapter adapter;
	private List<ICommand> list = new ArrayList<ICommand>();
	private View[] titleView;

	public CommandMenuBack(DialogAdapter adapter)
	{
		this.adapter = adapter;
		this.list.addAll(adapter.getList());
		this.titleView = adapter.getTitleViews();
	}

	@Override
	public String getName()
	{
		return "< –ß‚é";
	}

	@Override
	public void workOnUiThread()
	{
		adapter.setMenuItems(list);
		adapter.setTitle(titleView);
		adapter.createMenuDialog(false).show();
	}

}
