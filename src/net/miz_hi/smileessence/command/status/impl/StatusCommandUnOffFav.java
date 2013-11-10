package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.FavoriteTask;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

public class StatusCommandUnOffFav extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandUnOffFav(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "非公式ふぁぼ";
    }

    @Override
    public void workOnUiThread()
    {
        String str = "@" + status.user.screenName + " っ★";
        StatusUpdate update = new StatusUpdate(str);
        update.setInReplyToStatusId(status.statusId);
        new FavoriteTask(status.statusId).callAsync();
        new TweetTask(update).callAsync();
    }


    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canWarotaRT();
    }

}
