package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.UnFavoriteTask;

public class StatusCommandUnfavorite extends StatusCommand implements IHideable
{

    public StatusCommandUnfavorite(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "お気に入りを削除する";
    }

    @Override
    public void workOnUiThread()
    {
        new UnFavoriteTask(status.statusId).callAsync();
    }

}