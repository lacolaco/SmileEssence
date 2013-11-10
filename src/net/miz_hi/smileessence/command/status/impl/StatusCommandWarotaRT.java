package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.FavoriteTask;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

public class StatusCommandWarotaRT extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandWarotaRT(TweetModel model)
    {
        super(model);
    }

    @Override
    public String getName()
    {
        return "ワロタ式RT";
    }

    @Override
    public void workOnUiThread()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("ワロタｗ RT @");
        builder.append(status.user.screenName);
        builder.append(": ");
        builder.append(status.text);
        StatusUpdate update = new StatusUpdate(builder.toString());
        update.setInReplyToStatusId(status.statusId);
        new TweetTask(update).callAsync();
        new FavoriteTask(status.statusId).callAsync();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canWarotaRT() && !status.user.isProtected;
    }
}
