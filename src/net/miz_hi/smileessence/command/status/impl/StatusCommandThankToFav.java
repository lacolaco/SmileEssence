package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

public class StatusCommandThankToFav extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandThankToFav(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "ふぁぼあり";
    }

    @Override
    public void workOnUiThread()
    {
        String str = "@" + status.getOriginal().user.screenName + " ふぁぼあり(o^-')b";
        StatusUpdate update = new StatusUpdate(str);
        update.setInReplyToStatusId(status.getOriginal().statusId);
        new TweetTask(update).callAsync();
        status.getOriginal().favorite();
    }


    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canWarotaRT();
    }

}