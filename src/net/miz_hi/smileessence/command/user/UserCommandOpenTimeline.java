package net.miz_hi.smileessence.command.user;

import android.app.Activity;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.model.status.ResponseConverter;
import net.miz_hi.smileessence.model.status.user.UserModel;
import net.miz_hi.smileessence.model.statuslist.timeline.impl.UserTimeline;
import net.miz_hi.smileessence.statuslist.StatusListAdapter;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.system.PageController;
import net.miz_hi.smileessence.task.impl.GetUserTask;
import net.miz_hi.smileessence.view.fragment.impl.UserTimelineFragment;

public class UserCommandOpenTimeline extends UserCommand
{

    Activity activity;

    public UserCommandOpenTimeline(Activity activity, String userName)
    {
        super(userName);
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "ユーザーのタイムラインを開く";
    }

    @Override
    public void workOnUiThread()
    {
        MyExecutor.execute(new Runnable()
        {

            @Override
            public void run()
            {
                final UserModel model = ResponseConverter.convert(new GetUserTask(userName).call());
                UserTimeline timeline = new UserTimeline();
                StatusListManager.registerUserTimeline(model.userId, timeline, new StatusListAdapter(activity, timeline));
                UserTimelineFragment fragment = UserTimelineFragment.newInstance(model);
                PageController.getInstance().addPage(fragment);
                PageController.getInstance().moveToLast();
            }
        });
    }

}
