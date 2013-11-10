package net.miz_hi.smileessence.command.main;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.menu.MovePageMenu;
import net.miz_hi.smileessence.view.activity.MainActivity;


public class CommandToPageMove extends MenuCommand
{

    @Override
    public String getName()
    {
        return "タブを移動する";
    }

    @Override
    public void workOnUiThread()
    {
        new MovePageMenu(MainActivity.getInstance()).create().show();
    }

}
