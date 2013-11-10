package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.RetweetTask;

public class StatusCommandRetweet extends StatusCommand implements IConfirmable
{

    public StatusCommandRetweet(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "リツイート";
    }

    @Override
    public void workOnUiThread()
    {
        new RetweetTask(status.statusId).callAsync();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return !status.user.isProtected;
    }

}
