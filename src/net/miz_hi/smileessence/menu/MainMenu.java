package net.miz_hi.smileessence.menu;

import android.app.Activity;
import net.miz_hi.smileessence.command.CommandEditExtraWord;
import net.miz_hi.smileessence.command.CommandEditMenu;
import net.miz_hi.smileessence.command.CommandEditTemplate;
import net.miz_hi.smileessence.command.CommandReConnect;
import net.miz_hi.smileessence.command.main.*;
import net.miz_hi.smileessence.dialog.ExpandMenuDialog;

import java.util.ArrayList;
import java.util.List;

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

        MenuElement settingMenu = new MenuElement("設定");
        settingMenu.addChild(new MenuElement(new CommandOpenSetting(activity)));
        settingMenu.addChild(new MenuElement(new CommandEditTemplate(activity)));
        settingMenu.addChild(new MenuElement(new CommandEditExtraWord(activity)));
        settingMenu.addChild(new MenuElement(new CommandEditMenu(activity)));
        list.add(settingMenu);

        MenuElement tabMenu = new MenuElement("タブ操作");
        tabMenu.addChild(new MenuElement(new CommandToPageMove()));
        tabMenu.addChild(new MenuElement(new CommandClosePage()));
        tabMenu.addChild(new MenuElement(new CommandToAddPage()));
        list.add(tabMenu);

        MenuElement serviceMenu = new MenuElement("外部サービス");
        serviceMenu.addChild(new MenuElement(new CommandOpenFavstar(activity)));
        serviceMenu.addChild(new MenuElement(new CommandOpenAclog(activity)));
        serviceMenu.addChild(new MenuElement(new CommandOpenTwilog(activity)));
        list.add(serviceMenu);

        MenuElement otherMenu = new MenuElement("その他");
        otherMenu.addChild(new MenuElement(new CommandReConnect()));
        otherMenu.addChild(new MenuElement(new CommandCommercial()));
        otherMenu.addChild(new MenuElement(new CommandReport()));
        otherMenu.addChild(new MenuElement(new CommandFinish()));
        list.add(otherMenu);

        return list;
    }
}
