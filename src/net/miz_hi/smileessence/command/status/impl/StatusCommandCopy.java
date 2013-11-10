package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.FavoriteTask;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

public class StatusCommandCopy extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandCopy(TweetModel model)
    {
        super(model);
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canCopyTweet();
    }

    @Override
    public String getName()
    {
        return "パクる";
    }

    @Override
    public void workOnUiThread()
    {
        StatusUpdate update = new StatusUpdate(status.text);
        update.setInReplyToStatusId(status.inReplyToStatusId);
        new TweetTask(update).callAsync();
        new FavoriteTask(status.statusId).callAsync();
    }

}
