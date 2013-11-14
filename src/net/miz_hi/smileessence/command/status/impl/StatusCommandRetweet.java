package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

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
        status.retweet();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return !status.getOriginal().user.isProtected;
    }

}
