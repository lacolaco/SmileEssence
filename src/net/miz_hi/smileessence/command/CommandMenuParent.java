package net.miz_hi.smileessence.command;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.dialog.DialogAdapter;

public class CommandMenuParent extends MenuCommand
{

	private DialogAdapter adapter;
	private String text;
	private List<MenuCommand> list = new ArrayList<MenuCommand>();

	public CommandMenuParent(DialogAdapter adapter, String text, List<MenuCommand> list)
	{
		this.adapter = adapter;
		this.text = text;
		this.list.addAll(list);
	}

	@Override
	public String getName()
	{
		return text + " >";
	}

	@Override
	public void workOnUiThread()
	{
		if (!(list.get(0) instanceof CommandMenuBack))
		{
			list.add(0, new CommandMenuBack(adapter));
		}
		adapter.setMenuItems(list);
		adapter.setTitle(text);
		adapter.createMenuDialog(false).show();
	}
}
