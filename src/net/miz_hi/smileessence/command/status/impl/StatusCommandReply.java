package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.system.PostSystem;

public class StatusCommandReply extends StatusCommand
{

    public StatusCommandReply(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "返信";
    }

    @Override
    public void workOnUiThread()
    {
        PostSystem.setReply(status.user.screenName, status.statusId);
        PostSystem.openPostPage();
    }

}
