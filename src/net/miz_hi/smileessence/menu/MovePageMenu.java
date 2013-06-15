package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

import net.miz_hi.smileessence.command.CommandMovePage;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.util.NamedFragment;
import net.miz_hi.smileessence.view.activity.MainActivity;

public class MovePageMenu extends SimpleMenuDialog
{

	public MovePageMenu(Activity activity)
	{
		super(activity);
		setTitle("移動先のタブを選択");
	}

	@Override
	public List<ICommand> getMenuList()
	{
		List<ICommand> commands = new ArrayList<ICommand>();
		List<NamedFragment> pages = MainActivity.getInstance().getPagerAdapter().getList();
		for(int i = 0; i < pages.size(); i++)
		{
			NamedFragment fragment = pages.get(i);
			commands.add(new CommandMovePage(fragment.getTitle(), i));
		}
		return commands;
	}

}
