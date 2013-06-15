package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.CommandEditExtraWord;
import net.miz_hi.smileessence.command.CommandEditMenu;
import net.miz_hi.smileessence.command.CommandEditTemplate;
import net.miz_hi.smileessence.command.CommandReConnect;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.main.CommandCommercial;
import net.miz_hi.smileessence.command.main.CommandOpenFavstar;
import net.miz_hi.smileessence.command.main.CommandOpenSetting;
import net.miz_hi.smileessence.command.main.CommandReport;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import android.app.Activity;

public class MainMenu extends SimpleMenuDialog
{

	public MainMenu(Activity activity)
	{
		super(activity);
		setTitle("メインメニュー");
	}

	@Override
	public List<ICommand> getMenuList()
	{
		List<ICommand> list = new ArrayList<ICommand>();
		list.add(new CommandOpenSetting(activity));
		list.add(new CommandReConnect());
		list.add(new CommandEditTemplate(activity));
		list.add(new CommandEditExtraWord(activity));
		list.add(new CommandEditMenu(activity));
		list.add(new CommandOpenFavstar(activity));
		list.add(new CommandCommercial());
		list.add(new CommandReport());
		return list;
	}
}
