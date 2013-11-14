package net.miz_hi.smileessence.command.status.impl;

import android.app.Activity;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.core.MyExecutor;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.statuslist.StatusListAdapter;
import net.miz_hi.smileessence.statuslist.StatusListManager;
import net.miz_hi.smileessence.system.PageController;
import net.miz_hi.smileessence.talkchase.TalkChaser;
import net.miz_hi.smileessence.talkchase.TalkManager;
import net.miz_hi.smileessence.view.fragment.impl.TalkFragment;

public class StatusCommandChaseTalk extends StatusCommand
{

    Activity activity;

    public StatusCommandChaseTalk(Activity activity, TweetModel status)
    {
        super(status);
        this.activity = activity;
    }

    @Override
    public String getName()
    {
        return "会話をたどる";
    }

    @Override
    public void workOnUiThread()
    {
        final TweetModel origin = status.getOriginal();
        //すでにTalkが存在していればページを移動
        TalkChaser chaser = TalkManager.searchTalk(origin);
        if (chaser != null)
        {
            PageController.getInstance().move(PageController.getInstance().getAdapter().getItemPosition(chaser.fragment));
        }
        else
        {
            TalkFragment fragment = TalkFragment.newInstance(TalkManager.getNextTalkId(), origin.statusId);
            final TalkChaser chaser2 = new TalkChaser(fragment);
            TalkManager.addTalkChaser(chaser2);
            //StatusList管理に追加
            StatusListManager.registerTweetList(chaser2.talkList, new StatusListAdapter(activity, chaser2.talkList));
            MyExecutor.execute(new Runnable()
            {

                public void run()
                {
                    chaser2.startRelation(origin.statusId);
                }
            });

            PageController.getInstance().addPage(fragment);
            PageController.getInstance().moveToLast();
        }
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return status.getInReplyToStatusId() > -1;
    }


}
