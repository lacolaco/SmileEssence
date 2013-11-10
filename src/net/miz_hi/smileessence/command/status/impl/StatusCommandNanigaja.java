package net.miz_hi.smileessence.command.status.impl;

import net.miz_hi.smileessence.Client;
import net.miz_hi.smileessence.command.IConfirmable;
import net.miz_hi.smileessence.command.IHideable;
import net.miz_hi.smileessence.command.status.StatusCommand;
import net.miz_hi.smileessence.model.status.tweet.TweetModel;
import net.miz_hi.smileessence.task.impl.FavoriteTask;
import net.miz_hi.smileessence.task.impl.TweetTask;
import twitter4j.StatusUpdate;

public class StatusCommandNanigaja extends StatusCommand implements IHideable, IConfirmable
{

    public StatusCommandNanigaja(TweetModel status)
    {
        super(status);
    }

    @Override
    public String getName()
    {
        return "な～にが○○じゃ";
    }

    @Override
    public void workOnUiThread()
    {
        String base = status.text;
        if (base.startsWith("."))
        {
            base = base.replaceFirst(".", "");
        }
        if (base.startsWith("@" + Client.getMainAccount().getScreenName()))
        {
            base.replaceFirst(Client.getMainAccount().getScreenName(), status.user.screenName);
        }
        String str = "な～にが" + base.trim() + "じゃ";
        long id = status.statusId;
        new FavoriteTask(status.statusId).callAsync();
        StatusUpdate update = new StatusUpdate(str);
        update.setInReplyToStatusId(id);
        new TweetTask(update).callAsync();
    }

    @Override
    public boolean getDefaultVisibility()
    {
        return Client.getPermission().canWarotaRT();
    }

}
