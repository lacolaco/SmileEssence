package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

public class StatusCommandDelete extends StatusCommand implements IConfirmable
{

    public StatusCommandDelete(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "削除";
    }

    @Override
    public void workOnUiThread()
    {
        status.destroy();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return status.user.isMe() || status.getOriginal().user.isMe();
    }

}
