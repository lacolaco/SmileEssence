package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;

public class StatusCommandFavorite extends StatusCommand
{

    public StatusCommandFavorite(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "お気に入りに追加";
    }

    @Override
    public void workOnUiThread()
    {
        status.favorite();
    }

}
