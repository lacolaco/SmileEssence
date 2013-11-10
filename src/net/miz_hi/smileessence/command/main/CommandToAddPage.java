package net.miz_hi.smileessence.command.main;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.menu.AddPageMenu;
import net.miz_hi.smileessence.view.activity.MainActivity;


public class CommandToAddPage extends MenuCommand
{

    @Override
    public String getName()
    {
        return "タブを追加";
    }

    @Override
    public void workOnUiThread()
    {
        new AddPageMenu(MainActivity.getInstance()).create().show();
    }

}
