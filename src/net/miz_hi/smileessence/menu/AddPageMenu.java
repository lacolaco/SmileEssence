package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.CommandOpenUserList;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.user.CommandAddListPage;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import android.app.Activity;

public class AddPageMenu extends SimpleMenuDialog
{

	public AddPageMenu(Activity activity)
	{
		super(activity);
		setTitle("追加するタブを選択");
	}

	@Override
	public List<ICommand> getMenuList()
	{
		List<ICommand> commands = new ArrayList<ICommand>();
		
		commands.add(new CommandAddListPage(activity));
		
		
		return commands;
	}


}
