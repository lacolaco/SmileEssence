package net.miz_hi.smileessence.menu;

import android.app.Activity;
import net.miz_hi.smileessence.command.ICommand;
import net.miz_hi.smileessence.command.user.*;
import net.miz_hi.smileessence.dialog.SimpleMenuDialog;
import net.miz_hi.smileessence.model.status.user.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UserMenu extends SimpleMenuDialog
{

    private String userName;

    public UserMenu(Activity activity, UserModel model)
    {
        super(activity);
        this.userName = model.screenName;
        setTitle("@" + userName);
    }

    @Override
    public List<ICommand> getMenuList()
    {
        List<ICommand> items = new ArrayList<ICommand>();

        items.add(new UserCommandOpenTimeline(activity, userName));
        items.add(new UserCommandOpenPage(activity, userName));
        items.add(new UserCommandOpenFavstar(activity, userName));
        items.add(new UserCommandFollow(userName));
        items.add(new UserCommandUnfollow(userName));
        items.add(new UserCommandBlock(userName));
        items.add(new UserCommandSpam(userName));
        return items;
    }
}
