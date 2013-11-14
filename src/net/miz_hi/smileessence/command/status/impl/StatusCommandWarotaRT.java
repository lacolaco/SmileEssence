package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
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
        builder.append(status.getOriginal().user.screenName);
        builder.append(": ");
        builder.append(status.getText());
        StatusUpdate update = new StatusUpdate(builder.toString());
        update.setInReplyToStatusId(status.getOriginal().statusId);
        new TweetTask(update).callAsync();
        status.getOriginal().favorite();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canWarotaRT() && !status.getOriginal().user.isProtected;
    }
}
