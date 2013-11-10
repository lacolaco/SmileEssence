package net.miz_hi.smileessence.command;

import android.app.Activity;
import net.miz_hi.smileessence.model.statuslist.timeline.Timeline;
import net.miz_hi.smileessence.model.statuslist.timeline.impl.ListTimeline;
import net.miz_hi.smileessence.statuslist.StatusListAdapter;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.system.PageController;
import net.miz_hi.smileessence.view.fragment.impl.ListFragment;
import twitter4j.UserList;

public class CommandOpenUserList extends MenuCommand
{

    Activity activity;
    UserList userList;

    public CommandOpenUserList(Activity activity, UserList userList)
    {
        this.activity = activity;
        this.userList = userList;
    }

    @Override
    public String getName()
    {
        return userList.getFullName();
    }

    @Override
    public void workOnUiThread()
    {
        Timeline timeline = new ListTimeline();
        StatusListManager.registerListTimeline(userList.getId(), userList.getFullName(), timeline, new StatusListAdapter(activity, timeline));
        ListFragment fragment = ListFragment.newInstance(userList.getId(), userList.getFullName());
        PageController.getInstance().addPage(fragment);
        PageController.getInstance().moveToLast();
    }

}
