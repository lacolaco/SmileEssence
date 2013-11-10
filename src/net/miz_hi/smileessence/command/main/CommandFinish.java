package net.miz_hi.smileessence.command.main;

import net.miz_hi.smileessence.command.MenuCommand;
import net.miz_hi.smileessence.view.activity.MainActivity;


public class CommandFinish extends MenuCommand
{

    @Override
    public String getName()
    {
        return "アプリを終了する";
    }

    @Override
    public void workOnUiThread()
    {
        MainActivity.getInstance().finish(false);
    }

}
