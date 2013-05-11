package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.CommandEditExtraWord;
import net.miz_hi.smileessence.command.CommandEditMenu;
import net.miz_hi.smileessence.command.CommandEditTemplate;
import net.miz_hi.smileessence.command.CommandOpenPostPage;
import net.miz_hi.smileessence.command.CommandReConnect;
import net.miz_hi.smileessence.command.ICommand;
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
		setTitle("ÉÅÉCÉìÉÅÉjÉÖÅ[");
	}

	@Override
	public List<ICommand> getMenuList()
	{
		List<ICommand> items = new ArrayList<ICommand>();
		items.add(new CommandOpenSetting(activity));
		items.add(new CommandReConnect());
		items.add(new CommandEditTemplate(activity));
		items.add(new CommandEditExtraWord(activity));
		items.add(new CommandEditMenu(activity));
		items.add(new CommandOpenFavstar(activity));
		items.add(new CommandReport());
		return items;
	}
}
