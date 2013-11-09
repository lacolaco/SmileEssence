package net.miz_hi.smileessence.menu;

import android.app.Activity;
import net.miz_hi.smileessence.command.CommandMovePage;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.system.PageController;
import net.miz_hi.smileessence.view.fragment.NamedFragment;

import java.util.ArrayList;
import java.util.List;

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
        List<NamedFragment> pages = PageController.getInstance().getAdapter().getList();
        for (int i = 0; i < pages.size(); i++)
        {
            NamedFragment fragment = pages.get(i);
            commands.add(new CommandMovePage(fragment.getTitle(), i));
        }
        return commands;
    }

}
