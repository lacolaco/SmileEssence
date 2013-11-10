package net.miz_hi.smileessence.menu;

import android.app.Activity;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.user.CommandShowUserLists;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;

import java.util.ArrayList;
import java.util.List;

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

        commands.add(new CommandShowUserLists(activity));


        return commands;
    }


}
