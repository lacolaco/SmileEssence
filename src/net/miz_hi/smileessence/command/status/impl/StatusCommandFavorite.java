package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.FavoriteTask;

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
        new FavoriteTask(status.statusId).callAsync();
    }

}
