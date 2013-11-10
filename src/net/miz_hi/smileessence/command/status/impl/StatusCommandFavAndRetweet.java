package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.FavoriteTask;
import net.miz_hi.smileessence.task.impl.RetweetTask;

public class StatusCommandFavAndRetweet extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandFavAndRetweet(TweetModel model)
    {
        super(model);
    }

    @Override
    public String getName()
    {
        return "ふぁぼってリツイート";
    }

    @Override
    public void workOnUiThread()
    {
        new FavoriteTask(status.statusId).callAsync();
        new RetweetTask(status.statusId).callAsync();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return (!status.user.isMe() || (status.retweeter != null && !status.retweeter.isMe())) && !status.user.isProtected;
    }
}
