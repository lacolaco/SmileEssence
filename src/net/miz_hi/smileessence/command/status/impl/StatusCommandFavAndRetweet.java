package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

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
        status.favorite();
        status.retweet();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return !status.user.isProtected && !status.user.isMe() && !status.getOriginal().user.isMe();
    }
}
