package net.miz_hi.smileessence.menu;

import java.util.ArrayList;
import java.util.List;

import net.miz_hi.smileessence.command.CommandEditExtraWord;
import net.miz_hi.smileessence.command.CommandEditMenu;
import net.miz_hi.smileessence.command.CommandEditTemplate;
import net.miz_hi.smileessence.command.CommandReConnect;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.main.CommandClosePage;
import net.miz_hi.smileessence.command.main.CommandCommercial;
import net.miz_hi.smileessence.command.main.CommandFinish;
import net.miz_hi.smileessence.command.main.CommandOpenExtraPage;
import net.miz_hi.smileessence.command.main.CommandOpenSetting;
import net.miz_hi.smileessence.command.main.CommandReport;
import net.miz_hi.smileessence.command.main.CommandToAddPage;
import net.miz_hi.smileessence.command.main.CommandToPageMove;
import net.miz_hi.smileessence.dialog.ExpandMenuDialog;
import android.app.Activity;

public class MainMenu extends ExpandMenuDialog
{

	public MainMenu(Activity activity)
	{
		super(activity);
		setTitle("メインメニュー");
	}

	@Override
	public List<MenuElement> getElements()
	{
		List<MenuElement> list = new ArrayList<MenuElement>();
		
		list.add(new MenuElement(new CommandOpenSetting(activity)));
		list.add(new MenuElement(new CommandToPageMove()));		
		list.add(new MenuElement(new CommandEditTemplate(activity)));
		list.add(new MenuElement(new CommandEditExtraWord(activity)));
		list.add(new MenuElement(new CommandEditMenu(activity)));
		list.add(new MenuElement(new CommandFinish()));
		
		MenuElement tabMenu = new MenuElement("タブ操作");
		tabMenu.addChild(new MenuElement(new CommandClosePage()));
		tabMenu.addChild(new MenuElement(new CommandOpenExtraPage()));
		tabMenu.addChild(new MenuElement(new CommandToAddPage()));
		list.add(tabMenu);
		
		MenuElement otherMenu = new MenuElement("その他");
		otherMenu.addChild(new MenuElement(new CommandReConnect()));
		otherMenu.addChild(new MenuElement(new CommandCommercial()));
		otherMenu.addChild(new MenuElement(new CommandReport()));
		list.add(otherMenu);

		return list;
	}
}
