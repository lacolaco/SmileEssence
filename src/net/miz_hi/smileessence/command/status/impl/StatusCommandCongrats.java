package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

import java.util.Random;

public class StatusCommandCongrats extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandCongrats(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "お祝いする";
    }

    @Override
    public void workOnUiThread()
    {
        int favCount;
        Random rand = new Random();
        int r = rand.nextInt(100);
        if (r < 50)
        {
            favCount = 50;
        }
        else if (r < 80)
        {
            favCount = 100;
        }
        else if (r < 90)
        {
            favCount = 250;
        }
        else if (r < 99)
        {
            favCount = 1000;
        }
        else
        {
            favCount = 10000;
        }

        String str = String.format("@%s Congrats on your %s ★ tweet! http://favstar.fm/t/%s", status.getOriginal().user.screenName, favCount, status.getOriginal().statusId);
        StatusUpdate update = new StatusUpdate(str);
        update.setInReplyToStatusId(status.getOriginal().statusId);
        status.getOriginal().favorite();
        new TweetTask(update).callAsync();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canWarotaRT();
    }

}
